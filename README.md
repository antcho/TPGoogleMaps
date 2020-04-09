# TP Android : Google Maps

## Introduction
On connait tous Google Maps, nous allons aujourd'hui voir comment utiliser l'API Maps dans une application Android.

Pour suivre ce TP, veuillez cloner ce dépôt :

```bash
$ git clone https://github.com/antcho/TPGoogleMaps.git
```

La branche master est la branche sur laquelle travailler. Chaque étape est localisée grâce à un TODO dans Android Studio.

Vous pouvez également au besoin consulter une version terminée sur la branche final.

```bash
$ git checkout final
```

## Installation du SDK Google Play services

L'API est incluse dans le SDK Google Play services, disponible sur Android depuis la version 2.3. Il faut donc commencer par configurer Android Studio pour utiliser le SDK.

Dans Android Studio, ajouter le package de services Google Play :

Tools->Android->SDK Manager : onglet SDK Tools, cocher Google Play services, et cliquer sur Apply.

## Activer la clé api (TODO 1):

L'utilisation de l'API Maps nécessite une clé d'authentification liée à un projet sur un compte Google. Vous allez ici utiliser la mienne.

```xml
<string name="google_maps_key" translatable="false" templateMergeStrategy="preserve">AIzaSyDKQFkjt-YcuRci4W0xTo8NO1aDVQtNzjw</string>
```

##Activer ma localisation
On veut ici pouvoir afficher sur la map notre position actuelle.

###Demande de permission (TODO 2)

On utilise la demande de permission afin de demander à l'utilisateur l'autorisation d'utiliser sa localisation. Cette méthode teste si la permission est déjà accordée, si ce n'est pas le cas on la demande, sinon on utilise la méthode **`setMyLocationEnabled()`** pour mettre cette propriété de la map à vrai.

```java
private void enableMyLocation() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    } else if (mMap != null) {
        mMap.setMyLocationEnabled(true);
    }
}
```

### Gestion du résultat de la permission (TODO 3) :
On gère ici le retour de la demande de permissions. Si on les a obtenues, on appelle de nouveau la méthode **`enableMyLocation()`**.
```java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
        return;
    }
    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        enableMyLocation();
    } else {
        permissionDenied = true;
    }
}
```

### Afficher un message indiquant que les permissions n'ont pas été acceptées (TODO 4) :

```java
@Override
protected void onResumeFragments() {
    super.onResumeFragments();

    if (permissionDenied) {
        Toast.makeText(this, "Vous n'avez pas donné les permissions de localisation", Toast.LENGTH_SHORT).show();
        permissionDenied = false;
    }
}
```

Voilà pour la localisation. Vous pouvez à présent tester si ces étapes se sont bien déroulées.

## Gérer le clic sur la map :

On veut maintenant pouvoir cliquer à n'importe quel endroit de la map afin d'obtenir des informations sur ce lieu, et y afficher un marqueur.

### Créer le listener (TODO 5) :
Ici on override la méthode **`onMapClick()`** de l'interface **`OnMapClickListener`**. Cela permet de gérer l'événement clic sur la map, et d'obtenir le point sur lequel on a cliqué. La classe **`Geocoder`** permet de convertir des coordonnées en **`Address`**, à partir de  quoi on peut obtenir des informations de lieu. On ajoute ensuite un **`Marker`** sur la map à la position souhaitée, en ayant au préalable nettoyé la carte des précédents marqueurs.
```java
@Override
public void onMapClick(LatLng latLng) {
    List<Address> list;
    try {
        list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        editAddressTextView(list.get(0));

    } catch (IOException e) {
        e.printStackTrace();
    }
    mMap.clear();
    mMap.addMarker(new MarkerOptions().position(latLng).title("Vous avez cliqué ici"));
}
```

### Afficher l'addresse sur la page (TODO 6) :
Ici on récupère une **`Address`** et on affiche le nom du pays et de la ville auxquels elle appartient.
```java
private void editAddressTextView(Address address) {
        text.setText(
                "Pays:  " + address.getCountryName() + "\n" +
                "Ville: " + address.getLocality() + "\n");
}
```
Vous pouvez maitenant tester.

## Gérer la recherche de lieu (TODO 7) :

On attaque dans cette dernière partie la recherche de lieu. Au clic sur le bouton, on veut rechercher un lieu à partir du texte entré dans le champ texte. Tout va donc se passer dans la méthode **`onClick()`** :

```java
button.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
       try {
            //Tout se passe ici
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
});
```

- On commence par récupérer le nom de la localisation que l'on souhaite chercher.

```java
List<Address> listAddresses;
String locationName = editText.getText().toString();
```
- Grâce à **`geocoder`**, on récupère une liste d'adresse qui correspond à notre recherche, cependant nous ne voulons récupérer qu'un seul lieu.
```java
listAddresses = geocoder.getFromLocationName(locationName, 1);
Address address = listAddresses.get(0);
```
- On convertit ensuite notre adresse en coordonnées, ce qui va nous permettre de placer un marqueur.

```java
LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
mMap.clear();
mMap.addMarker(new MarkerOptions().position(location).title(locationName));
```
- On affiche les informations de l'adresse dans notre vue :
```java
editAddressTextView(address);
```
- La dernière étape consiste à faire bouger la caméra. On créé pour ça une nouvelle instance de **`CameraPosition`** située sur notre point, avec un certain zoom (la valeur 5, le reste des paramètres est là pour gérer l'orientation de la caméra).

```java
CameraPosition cameraPosition = new CameraPosition(location, 5, 0 ,0);
```
- Enfin, on crée une instance de **`CameraUpdate`** grâce à notre cameraPosition, ce qui permet ensuite d'animer la caméra en utilisant la méthode **`animateCamera()`** de notre map.

```java
CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
mMap.animateCamera(cameraUpdate);
```

## Conclusion

Nous avons donc vu les bases de l'interaction avec un objet Maps dans Android. N'hésitez pas à aller voir la documentation officielle pour aller plus loin.

## Liens

- https://developers.google.com/maps/documentation/android-api/start : documentation officielle
- https://github.com/googlemaps/android-samples : dépôt contenant un nombre conséquent d'exemples

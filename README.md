# TP Android : Google Maps

##Introduction
On connait tous Google Maps, voyons comment l'utiliser dans une application Android.

Tout d'abord, il faut configurer le projet pour utiliser le SDK Google Play services, qui inclut l'API Google Maps.

Dans Android Studio, ajouter le package de services Google Play : 

Tools->Android->SDK Manager : onglet SDK Tools, cocher Google Play services

Activer la clé api (TODO 1):

<string name="google_maps_key" translatable="false" templateMergeStrategy="preserve">AIzaSyDKQFkjt-YcuRci4W0xTo8NO1aDVQtNzjw</string>



Activer ma localisation : 

	Demande de permission (TODO 2) : 

	private void enableMyLocation() {
	    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
	        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
	    } else if (mMap != null) {
	        mMap.setMyLocationEnabled(true);
	    }
	}

	Gestion du résultat de la permission (TODO 3) :

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

	Afficher un message indiquant que les permissions n'ont pas été acceptées (TODO 4) :

	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();

	    if (permissionDenied) {
	        Toast.makeText(this, "Vous n'avez pas donné les permissions de localisation", Toast.LENGTH_SHORT).show();
	        permissionDenied = false;
	    }
	}

Gérer le clic sur la map : 

	Créer le listener (TODO 5) :

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

	Afficher l'addresse sur la page (TODO 6) :

	private void editAddressTextView(Address address) {
	        text.setText(
	                "Pays:  " + address.getCountryName() + "\n" +
	                "Ville: " + address.getLocality() + "\n");
	}

Gérer la recherche de lieu (TODO 7) : 

button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    List<Address> listAddresses;
                    String locationName = editText.getText().toString();
                    listAddresses = geocoder.getFromLocationName(locationName, 1);
                    Address address = listAddresses.get(0);
                    LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(location).title(locationName));

                    editAddressTextView(address);

                    CameraPosition cameraPosition = new CameraPosition(location, 5, 0 ,0);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

                    mMap.animateCamera(cameraUpdate);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


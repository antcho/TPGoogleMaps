package fr.lille1.raingeval.tpgooglemaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMyLocationButtonClickListener, OnMapClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private boolean permissionDenied = false;

    private Button button;
    private EditText editText;
    private TextView text;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.geocoder = new Geocoder(this, Locale.getDefault());
        this.button = (Button) findViewById(R.id.button);
        this.editText = (EditText) findViewById(R.id.edit);
        this.text = (TextView) findViewById((R.id.text));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO 7: Gérer le clic sur le bouton afin d'afficher sur la carte le lieu choisi
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);

        enableMyLocation();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Le bouton MyLocation a été cliqué", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //TODO 5: Au clic sur la map, récupérer les coordonnées du point, y placer un marqueur, et appeler la méthode editAddressTextView()
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //TODO 3: Gérer le résultat de la demande de permission de localisation
    }

    @Override
    protected void onResumeFragments() {
        //TODO 4: Si les permissions de localisation n'ont pas été acceptées, afficher un message
    }

    private void enableMyLocation() {
        //TODO 2: Autoriser Maps à accéder à la localisation de l'appareil, demander les permissions
    }

    //TODO 6: Créer la méthode editAddressTextView() pour afficher le nom du pays et de la ville de l'adresse
}

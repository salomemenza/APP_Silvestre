package com.silvestre.erp_mobile;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import com.silvestre.erp_mobile.global.variables_globales;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static int opcion;
    String Perfil;
    MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        item = findViewById(R.id.nav_message);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_header_txtusuario);
        TextView navuserPerfil = (TextView) headerView.findViewById(R.id.nav_header_txtperfil);
        TextView navEmpresa = (TextView) headerView.findViewById(R.id.nav_header_txtempresa);

        navUsername.setText(variables_globales.LOGIN_NOMBREUSUARIO);
        navuserPerfil.setText(variables_globales.LOGIN_PERFIL);
        Perfil = variables_globales.LOGIN_PERFIL;

        if (Perfil.compareTo("ADM. SISTEMAS") == 0 || Perfil.compareTo("CC SECTORISTA COORD") == 0 || Perfil.compareTo("GERENTE CC") == 0 || Perfil.compareTo("CC SECTORISTA") == 0) {

            navigationView.getMenu().findItem(R.id.nav_message).setVisible(true);
        }
        String EmpresaName = variables_globales.LOGIN_IDEMPRESA.equals("1") ? "SILVESTRE PERU S.A.C." : (variables_globales.LOGIN_IDEMPRESA.equals("2") ? "NEOAGRUM S.A.C." : "CLENVI S.A.C.");
        navEmpresa.setText(EmpresaName);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fmng = getSupportFragmentManager();

        if (id == R.id.nav_qr) {
            fmng.beginTransaction().replace(R.id.escenario, new fragment_registroqr()).commit();
        } else if (id == R.id.nav_message) {
            fmng.beginTransaction().replace(R.id.escenario, new fragment_enviosms()).commit();
        } else if (id == R.id.nav_letras) {
            fmng.beginTransaction().replace(R.id.escenario, new fragment_letras_historial()).commit();
        } else {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

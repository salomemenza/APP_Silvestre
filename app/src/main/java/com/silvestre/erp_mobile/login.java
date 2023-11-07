package com.silvestre.erp_mobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.model.tblusuario;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private RequestQueue queue;
    EditText txtusuario;
    EditText txtcontrasena;
    EditText txtEmpresa;
    Button btningresar;
    Button btnsalir;
    Button btninvitado;
    String token;
    private static tblusuario objUsuario = new tblusuario();
    ProgressDialog progressDialog = null;

    public static HttpsURLConnection urlConnection;

    /* Azure AD Variables */
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    private ImageButton btnMs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //handleSSLHandshake();
        this.printHashKey(getApplicationContext());

        txtusuario = (EditText) findViewById(R.id.txtusuario);
        txtcontrasena = (EditText) findViewById(R.id.txtcontraseña);
        btningresar = (Button) findViewById(R.id.btningresar);

        txtEmpresa = (EditText) findViewById(R.id.txtNombreEmpresa);
        //txtEmpresa.setInputType(InputType.TYPE_NULL);
        btnMs = (ImageButton) findViewById(R.id.btnMs);


        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(txtusuario.getText().toString().equals("admin") && txtcontrasena.getText().toString().equals("admin"))
//                {
//                    Intent intento= new Intent(getApplicationContext(),MainActivity.class);
//                    startActivityForResult(intento,REQUEST_SIGNUP);
//                }
                ConectarApi();
            }
        });

        txtEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialog = new AlertDialog.Builder(login.this, R.style.AlerDialogEstiloERP);
                alerDialog.setTitle("Seleccione Empresa");

                alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        txtEmpresa.setText("SILVESTRE");
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
                String[] types = {"SILVESTRE", "NEOAGRUM", "CLENVI"};

                alerDialog.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(which);
                        checkedItem.toString();
                        //Toast.makeText(getActivity(), checkedItem.toString(), Toast.LENGTH_SHORT).show();
                        txtEmpresa.setText(checkedItem.toString());

                    }

                });

                alerDialog.show();
            }
        });


        handleSSLHandshake("https://wsdynamics.gruposilvestre.com.pe:443");


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.SEND_SMS

                    }, PackageManager.PERMISSION_GRANTED);


        }


        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createSingleAccountPublicClientApplication(getApplicationContext(),
                R.raw.auth_config_single_account,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        /**
                         * This test app assumes that the app is only going to support one account.
                         * This requires "account_mode" : "SINGLE" in the config json file.
                         **/
                        mSingleAccountApp = application;
                        loadAccount();
                    }

                    @Override
                    public void onError(MsalException exception) {
//                        displayError(exception);
                        Toast.makeText(getBaseContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        btnMs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSingleAccountApp == null) {
                    return;
                }

                /*progressDialog = new ProgressDialog(this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_bar_personalizado);
                progressDialog.show();*/

                mSingleAccountApp.signIn(login.this, null, getScopes(), getAuthInteractiveCallback());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        /**
         * The account may have been removed from the device (if broker is in use).
         *
         * In shared device mode, the account might be signed in/out by other apps while this app is not in focus.
         * Therefore, we want to update the account state by invoking loadAccount() here.
         */
        loadAccount();
    }

    private String[] getScopes() {
        // Aquí, puedes definir y devolver los ámbitos (scopes) necesarios como un arreglo de cadenas.
        // Por ejemplo:
        return new String[]{"user.read"};
    }

    private AuthenticationCallback getAuthInteractiveCallback() {
        Log.d(TAG, "Entro a getAuthInteractiveCallback");
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getAccount().getClaims().get("id_token"));

                /* Update account */
                mAccount = authenticationResult.getAccount();
                Log.d(TAG, "Expire Token: " + authenticationResult.getExpiresOn());

                /* call graph */
                callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                //progressDialog.dismiss();

                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
//                displayError(exception);
                Toast.makeText(getBaseContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                //progressDialog.dismiss();
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    private void callGraphAPI(final IAuthenticationResult authenticationResult) {
        Log.d(TAG, "getAccessToken: " + authenticationResult.getAccessToken());
        MSGraphRequestWrapper.callGraphAPIUsingVolley(
                getApplicationContext(),
                authenticationResult.getAccessToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /* Successfully called graph, process data and send to UI */
                        Log.d(TAG, "Response: " + response.toString());
                        //progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response.toString());
                            variables_globales.LOGIN_PERFIL = obj.getString("mail");
                            variables_globales.LOGIN_NRODNI = "47256488";
                            variables_globales.LOGIN_NOMBREUSUARIO = obj.getString("displayName");
                            variables_globales.LOGIN_USUARIO = obj.getString("displayName");
                            variables_globales.LOGIN_IDEMPRESA = "1";
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //displayGraphResult(response);
                        //Realizar cierta accion
                        login();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Log.d(TAG, "Error: " + error.toString());
                        //displayError(error);
                        Toast.makeText(login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                mAccount = activeAccount;
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    Toast.makeText(login.this, "Signed Out.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Toast.makeText(login.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                //displayError(exception);
            }
        });
    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake(String URL_Silvestre) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

// Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(URL_Silvestre, session);
                }
            };

            URL url = new URL(URL_Silvestre);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(hostnameVerifier);
        } catch (Exception ignored) {
        }
    }

    private void copyInputStreamToFile(InputStream in, OutputStream file) {
        try {
            OutputStream out = file;
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ConectarApi() {
        final String usuario = txtusuario.getText().toString();
        final String password = txtcontrasena.getText().toString();

        if (usuario.isEmpty()) { //|| !android.util.Patterns.EMAIL_ADDRESS.matcher(usuario).matches()) {
            txtusuario.setError("Ingrese un usuario correcto");

        } else {
            txtusuario.setError(null);
        }

        if (password.isEmpty()) { // || password.length() < 4 || password.length() > 10) {
            txtcontrasena.setError("between 4 and 10 alphanumeric characters");

        } else {
            txtcontrasena.setError(null);
        }
        String url = variables_globales.URL_Login;

        try {
            // Create an HostnameVerifier that hardwires the expected hostname.
            // Note that is different than the URL's hostname:
            // example.com versus example.org
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("https://wsdynamics.gruposilvestre.com.pe:443", session);
                }
            };

            // Tell the URLConnection to use our HostnameVerifier
            URL url2 = new URL(url);
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection) url2.openConnection();
            urlConnection.setHostnameVerifier(hostnameVerifier);
            InputStream in = urlConnection.getInputStream();
            copyInputStreamToFile(in, System.out);
        } catch (Exception ex) {

        }


        variables_globales.LOGIN_IDEMPRESA = txtEmpresa.getText().toString().equals("SILVESTRE") ? "1" : "2";

        token = FirebaseInstanceId.getInstance().getToken();

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("Empresa", variables_globales.LOGIN_IDEMPRESA);
            postparams.put("Usuario", (Object) usuario);
            postparams.put("Password", password);
            postparams.put("Token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);
        progressDialog.show();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String respuesta = response.toString();
                        //Toast.makeText(login.this,response,Toast.LENGTH_LONG).show();
                        //{"d":"TRUE~398~ADM. SISTEMAS~46119959~SIS - ANDY VERA SANCHO"}
                        respuesta = respuesta.replace("\"", "").replace("d:", "").replace("{", "").replace("}", "");


                        String[] RespuestaArray = respuesta.split("~");
                        //objUsuario=parseData(response);

                        if (RespuestaArray[0].equals("FALSE")) {
                            progressDialog.dismiss();
                            Toast.makeText(login.this, "Usuario o Password incorrecto", Toast.LENGTH_SHORT).show();
                        } else {


                            variables_globales.LOGIN_PERFIL = RespuestaArray[2];
                            variables_globales.LOGIN_NRODNI = RespuestaArray[3];
                            variables_globales.LOGIN_NOMBREUSUARIO = RespuestaArray[4];


                            if (!variables_globales.LOGIN_NRODNI.equals("")) {
                                progressDialog.dismiss();
                                //variables_globales.LOGIN_CORREO=objUsuario.getEmail();
                                //variables_globales.LOGIN_NOMBREUSUARIO=objUsuario.getNomUsuario();
                                variables_globales.LOGIN_USUARIO = txtusuario.getText().toString();
                                login();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Usuario o Password incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error.networkResponse == null) {
                            Toast.makeText(login.this, "No hay conexión al servidor, verifique su conexión a internet.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(login.this, "No hay conexión al servidor.", Toast.LENGTH_LONG).show();
                        }

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);


    }

    public void login() {
        Log.d(TAG, "Login");
        /*
        if (!validate()) {
            onLoginFailed();
            return;
        }
        */

        btningresar.setEnabled(false);
        onLoginSuccess();
/*
        final ProgressDialog progressDialog = new ProgressDialog(login.this,
                R.style.Theme_AppCompat_DayNight);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        //String usuario = txtusuario.getText().toString();
        //String password = txtcontrasena.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
                */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btningresar.setEnabled(true);
        //public static final String LOGIN_USUARIO=txtusuario.toString().toUpperCase();
        //variables_globales.LOGIN_USUARIO = txtusuario.getText().toString().toUpperCase();

        Intent intento = new Intent(login.this, MainActivity.class);
        //startActivityForResult(intento,REQUEST_SIGNUP);
        startActivity(intento);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btningresar.setEnabled(true);
    }


    private void obtenerDatosVolley() {
        /*String url="direccion web";
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayobj= response.getJSONArray("contacts");

                    for (int i=0; i<jsonArrayobj.length();i++)
                    {
                        JSONObject jsonobj=jsonArrayobj.getJSONObject(i);
                        String name= jsonobj.getString("name");
                        Toast.makeText(login.this, name, Toast.LENGTH_SHORT).show();//imprime algo en la pantalla

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);*/




        /*
        if(objUsuario != null)
            return true;
        else
            return  false;
            */
    }

    public tblusuario parseData(JSONObject response) {
        tblusuario objtblUsuario = new tblusuario();
        try {


            JSONObject jsonObject = new JSONObject();
            //jsonObject=response;
            jsonObject = response;

            //if (jsonObject.getString("status").equals("true")) {
            Log.d("VOLLEY======>", jsonObject.toString());
            JSONArray dataArray = jsonObject.getJSONArray("d");

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);
                //Log.d("VOLLEY======>",dataobj.getString("nomUsuario"));
                objtblUsuario.setCodUsuario(Integer.parseInt(dataobj.getString("codUsuario")));
                objtblUsuario.setNomUsuario(dataobj.getString("nomUsuario"));
                objtblUsuario.setLoginUsuario(dataobj.getString("loginUsuario"));
                objtblUsuario.setContrasena(dataobj.getString("contrasena"));
                objtblUsuario.setActivo(Integer.parseInt(dataobj.getString("activo")));
                objtblUsuario.setEmail(dataobj.getString("email"));
                objtblUsuario.setRuc(dataobj.getString("ruc"));
            }


            //Intent intent = new Intent(login.this,MainActivity.class);
            //startActivity(intent);
            //}
            //return objtblUsuario;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return objtblUsuario;
        }


    }


    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        int orientation = getResources().getConfiguration().orientation;
        Log.d("CHANGESCREEN", "Orientation: " + orientation);
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Con la orientación en horizontal actualizamos el adaptador
                //adapter.notify();
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                // Con la orientación en vertical actualizamos el adaptador
                //adapter.notify();
                break;
        }
    }

    /**
     * Enables https connections
     *
     * @SuppressLint("TrulyRandom") public static void handleSSLHandshake() {
     * try {
     * TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
     * public X509Certificate[] getAcceptedIssuers() {
     * return new X509Certificate[0];
     * }
     * @Override public void checkClientTrusted(X509Certificate[] certs, String authType) {
     * }
     * @Override public void checkServerTrusted(X509Certificate[] certs, String authType) {
     * }
     * }};
     * <p>
     * SSLContext sc = SSLContext.getInstance("SSL");
     * sc.init(null, trustAllCerts, new SecureRandom());
     * HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
     * HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
     * @Override public boolean verify(String arg0, SSLSession arg1) {
     * return true;
     * }
     * });
     * } catch (Exception ignored) {
     * }
     * }
     */

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d("HashKey", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("salomon", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("salomon", "printHashKey()", e);
        }
    }
}

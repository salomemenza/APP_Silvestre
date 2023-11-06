package com.silvestre.erp_mobile.interfaz;

import com.android.volley.VolleyError;

public interface VolleyCallback {
    void onResponse(String idGenerado);
    void onErrorResponse(VolleyError error);
}

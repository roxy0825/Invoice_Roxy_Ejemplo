package com.example.invoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText name, ref, price, stock, idproduct;
    Button btnadd, btnsearch, btnupdate, btndelete, btnlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Referenciar los objetos
        idproduct = findViewById(R.id.eidproduct);
        name = findViewById(R.id.etname);
        ref = findViewById(R.id.etref);
        price = findViewById(R.id.etprice);
        stock = findViewById(R.id.etstock);
        btnadd = findViewById(R.id.btnadd);
        btnsearch = findViewById(R.id.btnsearch);
        btnupdate = findViewById(R.id.btnupdate);
        btndelete = findViewById(R.id.btndelete);
        btnlist = findViewById(R.id.btnlist);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteproduct(idproduct.getText().toString());
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateproduct(idproduct.getText().toString(),name.getText().toString(),ref.getText().toString(),price.getText().toString(),stock.getText().toString());
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readproduct(ref.getText().toString());
            }
        });
        //
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addproduct(name.getText().toString(),ref.getText().toString(),price.getText().toString(),stock.getText().toString());
            }
        });
    }

    private void deleteproduct(String uidproduct) {
        String url = "http://172.16.58.209/invoicing/product/deleteproduct.php?id=idproduct"+uidproduct;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Registro de Producto eliminado correctamente!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Registro de producto incorrecto!", Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

    private void updateproduct(String uidproduct, String uname, String uref, String uprice, String ustock) {
        if(!uname.isEmpty() && !uref.isEmpty() && !uprice.isEmpty() && !ustock.isEmpty()){
            //guardar el producto
            String url = "http://172.16.58.209/invoicing/product/updateproduct.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Registro de Producto realizado correctamente!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro de Producto incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idcust",uidproduct);
                    params.put("name",uname);
                    params.put("email", uref);
                    params.put("phone",uprice);
                    params.put("passwd",ustock);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
        else{
            Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
        }

    }

    *** private void readproduct(String fref) {
        String url = "http://172.16.58.209/invoicing/product/readproduct.php?email="+fref;
        // requermiento del servidor a traves de una api por el metodo get, manda la informacion en formato jSON ingresa en on response
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Product mProduct=new Product();

                JSONArray arrCustomer=response.optJSONArray("Product");
                JSONObject jsonObject=null;

                try {
                    jsonObject = arrCustomer.getJSONObject(0);//posición 0 del arreglo....
                    mProduct.setidproduct(jsonObject.optString("idproduct"));
                    mProduct.setName(jsonObject.optString("name"));
                    mProduct.setEmail(jsonObject.optString("email"));
                    mProduct.setPhone(jsonObject.optString("phone"));
                    mProduct.setPasswd(jsonObject.optString("passwd"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                idproduct.setText(mProduct.getIdcust());
                name.setText(mProduct.getName());//SE MODIFICA
                ref.setText(mProduct.getEmail());//SE MODIFICA
                price.setText(mProduct.getPhone());//SE MODIFICA
                stock.setText(mProduct.getPasswd());//SE MODIFICA
                Toast.makeText(getApplicationContext(),idproduct.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error, no se encuentra la Referencia: "+fref, Toast.LENGTH_LONG).show();
            }
        });
        // hacer la peticion por el metdo GET
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(jrq); // manda a ejecutar la linea anterior

    }

   ** private void addproduct(String name, String email, String phone, String passwd) {
        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !passwd.isEmpty()) {
            // guardar el cliente
            String url = "http://172.16.58.209/invoicing/product/addproduct.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Registro de producto realizado correctamente!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro de producto incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("phone", phone);
                    params.put("passwd", passwd);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
        else{
            Toast.makeText(getApplicationContext(),"Debe ingresar todos los datos...",Toast.LENGTH_SHORT).show();
        }
    }
}
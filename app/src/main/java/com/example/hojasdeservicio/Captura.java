package com.example.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class Captura extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText _edtTMarca, _edtTModelo, _edtTNoSerie, _edtTCambs, _edtTIP, _edtTMAC, _edtTDispositivo1, _edtTMDescripcionServicio;
    private CheckBox _chkBDispositivo;
    private Spinner _spnDispositivo, _spnRAM, _spnDD, _spnSO;
    private TextView _txtVMarca, _txtVModelo, _txtVNoSerie, _txtVCambs, _txtVIP, _txtVMAC, _txtVRAM, _txtVDD, _txtVSO, _txtVDispositivo1;
    private RadioGroup _rdGTipoDispositivo, _rdGCambs, _rdGRed, _rdGIP, _rdGRefacciones;
    private RadioButton _rdBPersonal, _rdBInstitucional, _rdBSCambs, _rdBNCambs, _rdBSRed, _rdBNRed, _rdBIPDinamica, _rdBIPEstatica, _rdBSRefacciones, _rdBNRefacciones;
    private Button _btnCapturaSN, _btnTomarFoto, _btnFinalizar, _btnGuardar;
    private ImageView _imgVEvidencia1, _imgVEvidencia2, _imgVEvidencia3, _imgVFirma;
    private ConstraintLayout _cnsLRAMDD, _cnsLRefacciones, _cnsLCambs, _cnsLRed, _cnsLEvidencia, _cnsLRadioBotonesInstitucional, _cnsLRadioBotonesIP;
    private int _itemSpinnerSeleccionado, _numServicio, _idElemento, _idDispositivo, _idTipo;
    private boolean _servicioCreado, _servicioTerminado;
    private SQLiteDatabase _db;
    private DataBase _dbHelper;
    private String _descripcion;
    private Servicio _servicio;
    private Dispositivo _dispositivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura);

        iniciarComponentes();

        try{
            //Conexión DB

            _dbHelper = new DataBase(getApplicationContext());

            _servicio = (Servicio) getIntent().getSerializableExtra("servicio");
            _dispositivo = new Dispositivo(getApplicationContext());
            _dispositivo.setIdDispositivo(_servicio.getIdDispositivo());
            _dispositivo.setInformacion();
            _numServicio = _servicio.getNumServicio();
            _servicioCreado = getIntent().getBooleanExtra("creado", false);
            _servicioTerminado = getIntent().getBooleanExtra("terminado", false);

            _idElemento = 0;
            _idDispositivo = 0;
            _idTipo = 0;

            //Ocultar al inicio y mostrar datos guardados

            ocultarDefault();

            Toast.makeText(this, _servicio.getNumServicio() + " numServicio", Toast.LENGTH_LONG).show();
            Toast.makeText(this, _servicio.getIdDispositivo() + " idDispositivo", Toast.LENGTH_LONG).show();
            Toast.makeText(this, _servicio.getContadorEvidencias() + " contador", Toast.LENGTH_LONG).show();

        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /*if(!_servicioCreado){
            //Datos de servicio
            _descripcion = getIntent().getStringExtra("descripcion");

            if(_numServicio != 0){
                if(servicioInventario()){
                    llenarInfoDispositivo();
                    _chkBDispositivo.setChecked(true);
                }
                if(_descripcion != null){
                    if(!_descripcion.equalsIgnoreCase("")){
                        _edtTMDescripcionServicio.setText(_descripcion);
                    }
                }
            }
        }

        if(_servicioTerminado){
            _btnFinalizar.setVisibility(View.GONE);
            _btnGuardar.setVisibility(View.GONE);
            bloquearEdicion();
            mostrarFirma();
        }*/
    }

    private void ocultarDefault(){
        _spnSO.setVisibility(View.GONE);
        _txtVMarca.setVisibility(View.GONE);
        _edtTMarca.setVisibility(View.GONE);
        _txtVModelo.setVisibility(View.GONE);
        _edtTModelo.setVisibility(View.GONE);
        _txtVNoSerie.setVisibility(View.GONE);
        _edtTNoSerie.setVisibility(View.GONE);
        _txtVCambs.setVisibility(View.GONE);
        _edtTCambs.setVisibility(View.GONE);
        _cnsLCambs.setVisibility(View.GONE);
        _cnsLRadioBotonesInstitucional.setVisibility(View.GONE);
        _txtVIP.setVisibility(View.GONE);
        _edtTIP.setVisibility(View.GONE);
        _txtVMAC.setVisibility(View.GONE);
        _edtTMAC.setVisibility(View.GONE);
        _cnsLRadioBotonesIP.setVisibility(View.GONE);
        _cnsLRed.setVisibility(View.GONE);
        _cnsLRAMDD.setVisibility(View.GONE);
        _txtVSO.setVisibility(View.GONE);
        _cnsLRefacciones.setVisibility(View.GONE);
        _btnCapturaSN.setVisibility(View.GONE);
        _txtVDispositivo1.setVisibility(View.GONE);
        _edtTDispositivo1.setVisibility(View.GONE);
        _cnsLEvidencia.setVisibility(View.GONE);
        _imgVEvidencia1.setVisibility(View.GONE);
        _imgVEvidencia2.setVisibility(View.GONE);
        _imgVEvidencia3.setVisibility(View.GONE);
        _imgVFirma.setVisibility(View.GONE);
    }

    private void mostrarInformacionDispositivos(){
        _txtVMarca.setVisibility(View.VISIBLE);
        _edtTMarca.setVisibility(View.VISIBLE);
        _txtVModelo.setVisibility(View.VISIBLE);
        _edtTModelo.setVisibility(View.VISIBLE);
        _txtVNoSerie.setVisibility(View.VISIBLE);
        _edtTNoSerie.setVisibility(View.VISIBLE);
        _cnsLRadioBotonesInstitucional.setVisibility(View.VISIBLE);
        _cnsLRed.setVisibility(View.VISIBLE);

        if(_itemSpinnerSeleccionado < 3){
            _cnsLRAMDD.setVisibility(View.VISIBLE);
            _txtVSO.setVisibility(View.VISIBLE);
            _spnSO.setVisibility(View.VISIBLE);
        } else{
            _cnsLRAMDD.setVisibility(View.GONE);
            _txtVSO.setVisibility(View.GONE);
            _spnSO.setVisibility(View.GONE);
        }
    }

    private void mostrarFirma(){
        _imgVFirma.setVisibility(View.VISIBLE);
        byte[] bFirma = null;
        _db = _dbHelper.getReadableDatabase();
        Cursor c = _db.rawQuery("SELECT firma FROM servicios WHERE num_servicio = ?", new String[]{_numServicio + ""});
        if(c != null){
            while(c.moveToNext()){
                bFirma = c.getBlob(0);
            }
        }
        float densidadPixeles = getApplicationContext().getResources().getDisplayMetrics().density;
        float valorWidth = 115*densidadPixeles;
        float valorHeight = 200*densidadPixeles;
        Bitmap bmp = BitmapFactory.decodeByteArray(bFirma, 0, bFirma.length);
        _imgVFirma.setImageBitmap(Bitmap.createScaledBitmap(bmp, (int) valorWidth, (int) valorHeight, false));
    }

    private void bloquearEdicion(){
        _chkBDispositivo.setEnabled(false);
        _spnDispositivo.setEnabled(false);
        _edtTMarca.setEnabled(false);
        _edtTModelo.setEnabled(false);
        _edtTNoSerie.setEnabled(false);
        _rdGTipoDispositivo.setEnabled(false);
        _rdGCambs.setEnabled(false);
        _edtTCambs.setEnabled(false);
        _rdGRed.setEnabled(false);
        _rdGIP.setEnabled(false);
        _edtTIP.setEnabled(false);
        _edtTMAC.setEnabled(false);
        _spnRAM.setEnabled(false);
        _spnDD.setEnabled(false);
        _spnSO.setEnabled(false);
        _rdGRefacciones.setEnabled(false);
        _btnCapturaSN.setEnabled(false);
        _edtTDispositivo1.setEnabled(false);
        _edtTMDescripcionServicio.setEnabled(false);
        _btnTomarFoto.setEnabled(false);
        _btnGuardar.setEnabled(false);
        _btnFinalizar.setEnabled(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.SpnDispositivo:
                _itemSpinnerSeleccionado = adapterView.getSelectedItemPosition();
                if(_itemSpinnerSeleccionado != 0){
                    mostrarInformacionDispositivos();
                } else{
                    ocultarDefault();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        ocultarDefault();
    }

    private boolean servicioInventario() {
        boolean atiendeDispositivo = false;
        _db = _dbHelper.getReadableDatabase();
        if (_db != null) {
            Cursor c = _db.rawQuery("SELECT dispositivos.id_elemento, dispositivos.id_dispositivo, dispositivos.id_tipo FROM servicio_inventario_dispositivos INNER JOIN dispositivos ON dispositivos.id_dispositivo = servicio_inventario_dispositivos.id_dispositivo WHERE num_servicio = ?", new String[]{_numServicio + ""});
            if(c != null){
                while(c.moveToNext()){
                    _idElemento = c.getInt(0);
                    _idDispositivo = c.getInt(1);
                    _idTipo = c.getInt(2);
                    atiendeDispositivo = true;
                    _spnDispositivo.setVisibility(View.VISIBLE);
                    _spnDispositivo.setSelection(_idTipo);
                }
            }
        }
        return atiendeDispositivo;
    }

    private void llenarInfoDispositivo(){
        _db = _dbHelper.getReadableDatabase();
        if(_db != null){
            Cursor c = _db.rawQuery("SELECT marca, modelo, num_serie FROM inventario WHERE id_elemento = ?", new String[]{_idElemento + ""});

            if(c != null) {
                while (c.moveToNext()) {
                    _edtTMarca.setText(c.getString(0));
                    _edtTModelo.setText(c.getString(1));
                    _edtTNoSerie.setText(c.getString(2));
                }
            }

            c = _db.rawQuery("SELECT cambs FROM equipos_institucionales WHERE id_dispositivo = ?", new String[]{_idDispositivo + ""});
            if(c != null){
                if(c.moveToNext()){
                    _rdGTipoDispositivo.check(R.id.RdBInstitucional);
                    if(c.getString(0) == null) {
                        _rdGCambs.check(R.id.RdBNCambs);
                    } else {
                        _rdGCambs.check(R.id.RdBSCambs);
                        _edtTCambs.setText(c.getString(0));
                    }
                } else{
                    _rdGTipoDispositivo.check(R.id.RdBPersonal);
                }
            }
            c = _db.rawQuery("SELECT id_conexion, ip, mac FROM conexiones WHERE id_dispositivo = ?", new String[]{_idDispositivo + ""});
            if(c != null){
                if(c.moveToNext()){
                    _rdGRed.check(R.id.RdBSRed);
                    if(c.getString(1) != null & !c.getString(1).equalsIgnoreCase("")){
                        _rdGIP.check(R.id.RdBIPEstatica);
                        _edtTIP.setText(c.getString(1));
                        _edtTMAC.setText(c.getString(2));
                    } else{
                        _rdGIP.check(R.id.RdBIPDinamica);
                    }
                } else{
                    _rdGRed.check(R.id.RdBNRed);
                }
            }

            c = _db.rawQuery("SELECT id_ram, id_disco_duro, id_so FROM computadoras WHERE id_elemento = ?", new String[]{_idElemento + ""});
            if(c != null){
                while(c.moveToNext()){
                    _spnRAM.setSelection(c.getInt(0));
                    _spnDD.setSelection(c.getInt(1));
                    _spnSO.setSelection(c.getInt(2));
                }
            }
        }
    }

    private void guardarRegistro(){
        ContentValues cv = new ContentValues();
        _db = _dbHelper.getWritableDatabase();
        if(_chkBDispositivo.isChecked()){
            if(_itemSpinnerSeleccionado != 0){
                if(String.valueOf(_edtTNoSerie.getText()).equalsIgnoreCase("")){
                    Toast.makeText(this, "Ingrese el número de serie del dispositivo.", Toast.LENGTH_LONG).show();
                    return;
                } else{
                    _db = _dbHelper.getReadableDatabase();
                    if(_db != null){
                        Cursor c = _db.rawQuery("SELECT id_elemento FROM inventario WHERE num_serie = ?", new String[]{String.valueOf(_edtTNoSerie.getText())});
                        if(c.moveToNext()){
                            _idElemento = c.getInt(0);
                        }
                    }
                    _db = _dbHelper.getWritableDatabase();
                    if(_db != null){
                        cv.put("marca", String.valueOf(_edtTMarca.getText()));
                        cv.put("modelo", String.valueOf(_edtTModelo.getText()));
                        cv.put("num_serie", String.valueOf(_edtTNoSerie.getText()));

                        if(_idElemento == 0){
                            _idElemento = (int) _db.insert("inventario", null, cv);
                            cv.clear();

                            cv.put("id_elemento", _idElemento);
                            cv.put("id_tipo", _itemSpinnerSeleccionado);
                            _idDispositivo = (int) _db.insert("dispositivos", null, cv);
                            cv.clear();

                            cv.put("num_servicio", _numServicio);
                            cv.put("id_dispositivo", _idDispositivo);
                            _db.insert("servicio_inventario_dispositivos", null, cv);
                            cv.clear();

                            if(_itemSpinnerSeleccionado < 3){
                                cv.put("id_elemento", _idElemento);
                                cv.put("id_ram", _spnRAM.getSelectedItemPosition());
                                cv.put("id_disco_duro", _spnDD.getSelectedItemPosition());
                                cv.put("id_so", _spnSO.getSelectedItemPosition());
                                _db.insert("computadoras", null, cv);
                                cv.clear();
                            }

                            if (_rdBInstitucional.isChecked()){
                                cv.put("id_dispositivo", _idDispositivo);
                                if(_rdBSCambs.isChecked()){
                                    cv.put("cambs", String.valueOf(_edtTCambs.getText()));
                                }
                                _db.insert("equipos_institucionales", null, cv);
                                cv.clear();
                            }

                            if(_rdBSRed.isChecked()){
                                cv.put("id_dispositivo", _idDispositivo);
                                cv.put("mac", String.valueOf(_edtTMAC.getText()));
                                if(_rdBIPEstatica.isChecked()){
                                    cv.put("ip", String.valueOf(_edtTIP.getText()));
                                }
                                _db.insert("conexiones", null, cv);
                            }

                        } else{
                            _db.update("inventario", cv, "id_elemento = ?", new String[]{_idElemento + ""});
                            cv.clear();

                            cv.put("id_tipo", _itemSpinnerSeleccionado);
                            _db.update("dispositivos", cv, "id_elemento = ?", new String[]{_idElemento + ""});
                            cv.clear();

                            if(_rdBInstitucional.isChecked()){
                                cv.put("id_dispositivo", _idDispositivo);
                                if(_db.update("equipos_institucionales", cv, "id_dispositivo = ?", new String[]{_idDispositivo + ""}) == 0){
                                    cv.put("id_dispositivo", _idDispositivo);
                                    _db.insert("equipos_institucionales", null, cv);
                                    cv.clear();
                                }
                                if(_rdBSCambs.isChecked()){
                                    cv.put("cambs", String.valueOf(_edtTCambs.getText()));
                                    _db.update("equipos_institucionales", cv, "id_dispositivo = ?", new String[]{_idDispositivo + ""});
                                }
                                cv.clear();
                            } else{
                                _db.delete("equipos_institucionales", "id_dispositivo = ?", new String[]{_idDispositivo + ""});
                            }

                            if(_itemSpinnerSeleccionado < 3){
                                cv.put("id_ram", _spnRAM.getSelectedItemPosition());
                                cv.put("id_disco_duro", _spnDD.getSelectedItemPosition());
                                cv.put("id_so", _spnSO.getSelectedItemPosition());
                                if(_db.update("computadoras", cv, "id_elemento = ?", new String[]{_idElemento + ""}) == 0){
                                    cv.put("id_elemento", _idElemento);
                                    _db.insert("computadoras", null, cv);
                                }
                                cv.clear();
                            } else{
                                _db.delete("computadoras", "id_elemento = ?", new String[]{_idElemento + ""});
                            }

                            if(_rdBSRed.isChecked()){
                                cv.put("mac", String.valueOf(_edtTMAC.getText()));
                                if(_rdBIPEstatica.isChecked()){
                                    cv.put("ip", String.valueOf(_edtTIP.getText()));
                                }
                                if(_db.update("conexiones", cv, "id_dispositivo = ?", new String[]{_idDispositivo + ""}) == 0){
                                    cv.put("id_dispositivo", _idDispositivo);
                                    _db.insert("conexiones", null, cv);
                                }
                                cv.clear();
                            } else{
                                _db.delete("conexiones", "id_dispositivo = ?", new String[]{_idDispositivo + ""});
                            }
                        }
                    }
                }
            } else{
                Toast.makeText(this, "Seleccione un componente", Toast.LENGTH_LONG).show();
                return;
            }
        } else{
            _db.delete("servicio_inventario_dispositivos", "num_servicio = ?", new String[]{_numServicio + ""});
        }
        if(String.valueOf(_edtTMDescripcionServicio.getText()).equalsIgnoreCase("")){
            Toast.makeText(this, "Describa el servicio realizado.", Toast.LENGTH_LONG).show();
        } else{
            cv.put("descripcion_servicio", String.valueOf(_edtTMDescripcionServicio.getText()));
            _db.update("servicios", cv, "num_servicio = ?", new String[]{_numServicio + ""});
            Toast.makeText(this, "Guardado exitoso.", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarComponentes(){
        //Todos los spinners

        _spnDispositivo = findViewById(R.id.SpnDispositivo);//Spinner "Dispositivos"
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dispositivos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spnDispositivo.setAdapter(adapter);
        _spnDispositivo.setOnItemSelectedListener(this);
        _spnDispositivo.setVisibility(View.GONE);

        _spnRAM = findViewById(R.id.SpnRAM);
        adapter = ArrayAdapter.createFromResource(this, R.array.ram, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spnRAM.setAdapter(adapter);
        _spnRAM.setOnItemSelectedListener(this);

        _spnDD = findViewById(R.id.SpnDD);
        adapter = ArrayAdapter.createFromResource(this, R.array.dd, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spnDD.setAdapter(adapter);
        _spnDD.setOnItemSelectedListener(this);

        _spnSO = findViewById(R.id.SpnSO);
        adapter = ArrayAdapter.createFromResource(this, R.array.so, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spnSO.setAdapter(adapter);
        _spnSO.setOnItemSelectedListener(this);

        //CheckBox "¿Atiendes un dispositivo?

        _chkBDispositivo = findViewById(R.id.ChkBDispositivo);
        _chkBDispositivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    _spnDispositivo.setVisibility(View.VISIBLE);//Revela Spinner
                } else{
                    _spnDispositivo.setSelection(0);
                    _spnDispositivo.setVisibility(View.GONE);
                    ocultarDefault();
                    //_spnDispositivo.setVisibility(View.GONE);
                }
            }
        });

        //Información génerica de dispositivos

        _txtVMarca = findViewById(R.id.TxtVMarca);
        _edtTMarca = findViewById(R.id.EdtTMarca);
        _txtVModelo = findViewById(R.id.TxtVModelo);
        _edtTModelo = findViewById(R.id.EdtTModelo);
        _txtVNoSerie = findViewById(R.id.TxtVNoSerie);
        _edtTNoSerie = findViewById(R.id.EdtTNoSerie);

        //Sobre CAMBS

        _txtVCambs = findViewById(R.id.TxtVCambs);
        _edtTCambs = findViewById(R.id.EdtTCambs);
        _cnsLCambs = findViewById(R.id.CnsLCambs);
        _rdGCambs = findViewById(R.id.RdGCambs);
        _rdGCambs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.RdBSCambs:
                        _txtVCambs.setVisibility(View.VISIBLE);
                        _edtTCambs.setVisibility(View.VISIBLE);
                        break;
                    case R.id.RdBNCambs:
                        _txtVCambs.setVisibility(View.GONE);
                        _edtTCambs.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        //Constraint Layout que contiene radio botones "Personal" e "Institucional"

        _cnsLRadioBotonesInstitucional = findViewById(R.id.CnsLRadioBotonesInstitucional);
        _rdGTipoDispositivo = findViewById(R.id.RdGTipoDispositivo);
        _rdGTipoDispositivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.RdBPersonal:
                        _cnsLCambs.setVisibility(View.GONE);
                        break;
                    case R.id.RdBInstitucional:
                        _cnsLCambs.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
        _rdBInstitucional = findViewById(R.id.RdBInstitucional);
        _rdBPersonal = findViewById(R.id.RdBPersonal);
        _rdBSCambs = findViewById(R.id.RdBSCambs);
        _rdBNCambs = findViewById(R.id.RdBNCambs);

        //Campos de IP

        _txtVIP = findViewById(R.id.TxtVIP);
        _edtTIP = findViewById(R.id.EdtTIP);
        _txtVMAC = findViewById(R.id.TxtVMAC);
        _edtTMAC = findViewById(R.id.EdtTMAC);

        //Constraint Layout con radio botones "IP dinámica" e "IP estática"

        _cnsLRadioBotonesIP = findViewById(R.id.CnsLRadioBotonesIP);
        _rdGIP = findViewById(R.id.RdGIP);
        _rdGIP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.RdBIPEstatica:
                        _txtVIP.setVisibility(View.VISIBLE);
                        _edtTIP.setVisibility(View.VISIBLE);
                        break;
                    case R.id.RdBIPDinamica:
                        _txtVIP.setVisibility(View.GONE);
                        _edtTIP.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
        _rdBIPEstatica = findViewById(R.id.RdBIPEstatica);
        _rdBIPDinamica = findViewById(R.id.RdBIPDinamica);

        //Constraint Layout con radio botones de red

        _cnsLRed = findViewById(R.id.CnsLRed);
        _rdGRed = findViewById(R.id.RdGRed);
        _rdGRed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.RdBSRed:
                        _cnsLRadioBotonesIP.setVisibility(View.VISIBLE);
                        _txtVMAC.setVisibility(View.VISIBLE);
                        _edtTMAC.setVisibility(View.VISIBLE);
                        break;
                    case R.id.RdBNRed:
                        _cnsLRadioBotonesIP.setVisibility(View.GONE);
                        _txtVMAC.setVisibility(View.GONE);
                        _edtTMAC.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        _rdBSRed = findViewById(R.id.RdBSRed);
        _rdBNRed = findViewById(R.id.RdBNRed);

        //Constraint de campos de PC

        _cnsLRAMDD = findViewById(R.id.CnsLRamDD);
        _txtVSO = findViewById(R.id.TxtVSO);

        //Constraint de refacciones

        _cnsLRefacciones = findViewById(R.id.CnsLRefacciones);
        _rdGRefacciones = findViewById(R.id.RdGRefacciones);
        _btnCapturaSN = findViewById(R.id.BtnCapturarSN);
        _txtVDispositivo1 = findViewById(R.id.TxtVDispositivo1);
        _edtTDispositivo1 = findViewById(R.id.EdtTDispositivo1);

        //Descripcion del servicio

        _edtTMDescripcionServicio = findViewById(R.id.EdtTMDescripcionServicio);

        //Fotos

        _btnTomarFoto = findViewById(R.id.BtnTomarFoto);
        _cnsLEvidencia = findViewById(R.id.CnsLEvidencia);
        _imgVEvidencia1 = findViewById(R.id.ImgVEvidencia1);
        _imgVEvidencia2 = findViewById(R.id.ImgVEvidencia2);
        _imgVEvidencia3 = findViewById(R.id.ImgVEvidencia3);
        _imgVFirma = findViewById(R.id.ImgVFirma);

        //Botones finales

        _btnFinalizar = findViewById(R.id.BtnFinalizar);
        _btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarRegistro();
                Intent intent = new Intent(getApplicationContext(), Firma.class);
                intent.putExtra("numServicio", _numServicio);
                intent.putExtra("descripcion", _descripcion);
                startActivity(intent);
                finish();
            }
        });
        _btnGuardar = findViewById(R.id.BtnGuardar);
        _btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarRegistro();
            }
        });
    }
}
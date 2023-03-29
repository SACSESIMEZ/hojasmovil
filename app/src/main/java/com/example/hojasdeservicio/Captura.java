package com.example.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class Captura extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText _edtTMarca, _edtTModelo, _edtTNoSerie, _edtTCambs, _edtTIP, _edtTMAC, _edtTDispositivo1, _edtTMDescripcionServicio;
    private CheckBox _chkBDispositivo;
    private Spinner _spnDispositivo, _spnRAM, _spnDD, _spnSO;
    private TextView _txtVMarca, _txtVModelo, _txtVNoSerie, _txtVTieneCambs, _txtVCambs, _txtVIP, _txtVMAC, _txtVRAM, _txtVDD, _txtVSO, _txtVDispositivo1;
    private RadioGroup _rdGTipoDispositivo, _rdGCambs, _rdGRed, _rdGIP, _rdGRefacciones;
    private RadioButton _rdBPersonal, _rdBInstitucional, _rdBSCambs, _rdBNCambs, _rdBSRed, _rdBNRed, _rdBIPDinamica, _rdBIPEstatica, _rdBSRefacciones, _rdBNRefacciones;
    private Button _btnCapturaSN, _btnTomarFoto, _btnFinalizar, _btnGuardar;
    private ImageView _imgVEvidencia1, _imgVEvidencia2, _imgVEvidencia3, _imgVFirma;
    private ConstraintLayout _cnsLRAMDD, _cnsLRefacciones, _cnsLCambs, _cnsLRed, _cnsLEvidencia, _cnsLRadioBotonesInstitucional, _cnsLRadioBotonesIP;
    private int _itemSpinnerSeleccionado, _idServicio, _idElemento, _idDispositivo, _idTipo;
    private SQLiteDatabase _db;
    private DataBase _dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura);

        //Conexión DB

        _dbHelper = new DataBase(getApplicationContext());

        //Todos los spinners

        _spnDispositivo = findViewById(R.id.SpnDispositivo);//Spinner "Dispositivos"
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dispositivos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spnDispositivo.setAdapter(adapter);
        _spnDispositivo.setOnItemSelectedListener(this);

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
                    ocultarDefault();
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

        _cnsLRAMDD = findViewById(R.id.CnsLRamDD);

        _txtVSO = findViewById(R.id.TxtVSO);

        _cnsLRefacciones = findViewById(R.id.CnsLRefacciones);

        _rdGRefacciones = findViewById(R.id.RdGRefacciones);

        _btnCapturaSN = findViewById(R.id.BtnCapturarSN);

        _txtVDispositivo1 = findViewById(R.id.TxtVDispositivo1);

        _edtTDispositivo1 = findViewById(R.id.EdtTDispositivo1);

        _edtTMDescripcionServicio = findViewById(R.id.EdtTMDescripcionServicio);

        _btnTomarFoto = findViewById(R.id.BtnTomarFoto);

        _cnsLEvidencia = findViewById(R.id.CnsLEvidencia);

        _imgVEvidencia1 = findViewById(R.id.ImgVEvidencia1);

        _imgVEvidencia2 = findViewById(R.id.ImgVEvidencia2);

        _imgVEvidencia3 = findViewById(R.id.ImgVEvidencia3);

        _imgVFirma = findViewById(R.id.ImgVFirma);

        _btnFinalizar = findViewById(R.id.BtnFinalizar);

        _btnGuardar = findViewById(R.id.BtnGuardar);

        ocultarDefault();

        //Datos de servicio

        _idServicio = getIntent().getIntExtra("idServicio", 0);

        if(_idServicio != 0){
            if(servicioInventario()){
                llenarInfoDispositivo();
                _chkBDispositivo.setChecked(true);
                _spnDispositivo.setSelection(_idTipo);
                mostrarInformacionDispositivos();
            }
        }

    }

    private void ocultarDefault(){
        _spnDispositivo.setVisibility(View.GONE);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        _itemSpinnerSeleccionado = adapterView.getSelectedItemPosition();
        switch (adapterView.getId()){
            case R.id.SpnDispositivo:
                if(_itemSpinnerSeleccionado != 0){
                    mostrarInformacionDispositivos();
                } else{
                    ocultarDefault();
                    _spnDispositivo.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private boolean servicioInventario() {
        boolean atiendeDispositivo = false;
        _db = _dbHelper.getReadableDatabase();
        if (_db != null) {
            Cursor c = _db.rawQuery("SELECT dispositivos.id_elemento, dispositivos.id_dispositivo, dispositivos.id_tipo FROM servicio_inventario_dispositivos INNER JOIN dispositivos ON dispositivos.id_dispositivo = servicio_inventario_dispositivos.id_dispositivo WHERE num_servicio = ?", new String[]{_idServicio + ""});
            if(c != null){
                while(c.moveToNext()){
                    _idElemento = c.getInt(0);
                    _idDispositivo = c.getInt(1);
                    _idTipo = c.getInt(2);
                    atiendeDispositivo = true;
                }
            }
        }
        return atiendeDispositivo;
    }

    private void llenarInfoDispositivo(){
        _db = _dbHelper.getReadableDatabase();
        if(_db != null){
            Cursor c = _db.rawQuery("SELECT marca, modelo, num_serie FROM inventario WHERE id_elemento = ?", new String[]{_idElemento + ""});
            if(c != null){
                while(c.moveToNext()){
                    _edtTMarca.setText(c.getString(0));
                    _edtTModelo.setText(c.getString(1));
                    _edtTNoSerie.setText(c.getString(2));
                }
            }
            
            c = _db.rawQuery("SELECT cambs FROM equipos_institucionales WHERE id_dispositivo = ?", new String[]{_idDispositivo + ""});
            if(c != null){
                while(c.moveToNext()){
                    Integer idDispositivo = c.getInt(1);
                    if(idDispositivo != null && idDispositivo != 0){
                        _rdGTipoDispositivo.check(R.id.RdBInstitucional);
                        if(c.getString(0) != null && !c.getString(0).equalsIgnoreCase("")){
                            _rdGCambs.check(R.id.RdBSCambs);
                            _edtTCambs.setText(c.getString(0));
                        } else{
                            _rdGCambs.check(R.id.RdBNCambs);
                        }
                    } else{
                        _rdGTipoDispositivo.check(R.id.RdBPersonal);
                    }
                }
            }
            c = _db.rawQuery("SELECT id_conexion, ip, mac FROM conexiones WHERE id_dispositivo = ?", new String[]{_idDispositivo + ""});
            if(c != null){
                while (c.moveToNext()){
                    Integer idConexion = c.getInt(0);
                    if(idConexion != null && idConexion != 0){
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
            }

            c = _db.rawQuery("SELECT id_computadoras, id_ram, id_disco_duro, id_so FROM computadoras WHERE id_dispositivo = ?", new String[]{_idDispositivo + ""});
            if(c != null){
                while(c.moveToNext()){
                    Integer idComputadora = c.getInt(0);
                    if(idComputadora != null && idComputadora != 0){
                        _spnRAM.setSelection(c.getInt(1));
                        _spnDD.setSelection(c.getInt(2));
                        _spnSO.setSelection(c.getInt(3));
                    }
                }
            }
        }
    }
}
package com.example.hojasdeservicio;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
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

import com.example.hojasdeservicio.databinding.ActivityCapturaBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Captura extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText _edtTMarca, _edtTModelo, _edtTNoSerie, _edtTCambs, _edtTIP, _edtTMAC, _edtTDispositivo1, _edtTMDescripcionServicio;
    private CheckBox _chkBDispositivo;
    private Spinner _spnDispositivo, _spnRAM, _spnDD, _spnSO;
    private TextView _txtVMarca, _txtVModelo, _txtVNoSerie, _txtVCambs, _txtVIP, _txtVMAC, _txtVSO, _txtVDispositivo1;
    private RadioGroup _rdGTipoDispositivo, _rdGCambs, _rdGRed, _rdGIP, _rdGRefacciones;
    private RadioButton _rdBPersonal, _rdBInstitucional, _rdBSCambs, _rdBNCambs, _rdBSRed, _rdBNRed, _rdBIPDinamica, _rdBIPEstatica, _rdBSRefacciones, _rdBNRefacciones;
    private Button _btnCapturaSN, _btnTomarFoto, _btnFinalizar, _btnGuardar;
    private ImageView _imgVFirma;
    private ImageView[] _imgEvidencias;
    private ConstraintLayout _cnsLRAMDD, _cnsLRefacciones, _cnsLCambs, _cnsLRed, _cnsLEvidencia, _cnsLRadioBotonesInstitucional, _cnsLRadioBotonesIP;
    private int _itemSpinnerSeleccionado, _numServicio, _evidenciasGuardadas;
    private boolean _servicioCreado, _servicioTerminado;
    private SQLiteDatabase _db;
    private DataBase _dbHelper;
    private String _descripcion;
    private Servicio _servicio;
    private static int REQUEST_IMAGE_CAPTURE = 1;
    private ActivityCapturaBinding _mainBinding;
    private ActivityResultLauncher<Uri> _takePictureLauncher;
    private Uri _imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura);

        iniciarComponentes();

        //Conexión DB

        _dbHelper = new DataBase(getApplicationContext());

        _numServicio = getIntent().getIntExtra("numServicio", 0);
        _servicioCreado = getIntent().getBooleanExtra("creado", false);
        _servicioTerminado = getIntent().getBooleanExtra("terminado", false);

        _servicio = new Servicio(this);
        _servicio.setNumServicio(_numServicio);
        _servicio.buscarInformacion();

        //Contador de evidencias guardadas

        _evidenciasGuardadas = _servicio.getContadorEvidencias();

        //Ocultar al inicio y mostrar datos guardados

        ocultarDefault();

        if(!_servicioCreado){
            //Datos de servicio
            if(_servicio.isDispositivoServicio()){
                mostrarInformacionDispositivos();
                llenarInfoDispositivo();
            }
            for(int i = 0; i < _servicio.getContadorEvidencias(); i++){
                Bitmap bmp = BitmapFactory.decodeByteArray(_servicio.getEvidencias(i), 0, _servicio.getEvidencias(i).length);
                _imgEvidencias[i].setVisibility(View.VISIBLE);
                _imgEvidencias[i].setImageBitmap(bmp);
            }
            _edtTMDescripcionServicio.setText(_servicio.getDescripcionServicio());
        }

        if(_servicioTerminado){
            _btnFinalizar.setVisibility(View.GONE);
            _btnTomarFoto.setVisibility(View.GONE);
            _chkBDispositivo.setVisibility(View.GONE);
            _btnGuardar.setVisibility(View.GONE);
            bloquearEdicion();
            mostrarFirma();
        }
    }

    private void llenarInfoDispositivo(){
        _chkBDispositivo.setChecked(true);
        _spnDispositivo.setSelection(_servicio.getDispositivo().getIdTipo());
        _edtTMarca.setText((_servicio.getDispositivo().getMarca() == null) ? "" : _servicio.getDispositivo().getMarca());
        _edtTModelo.setText((_servicio.getDispositivo().getModelo() == null) ? "" : _servicio.getDispositivo().getModelo());
        _edtTNoSerie.setText((_servicio.getDispositivo().getNumSerie() == null) ? "" : _servicio.getDispositivo().getNumSerie());
        if(_servicio.getDispositivo().isInstitucional()){
            _rdBInstitucional.setChecked(true);
            if(_servicio.getDispositivo().getCambs() != null){
                _rdBSCambs.setChecked(true);
                _edtTCambs.setText(_servicio.getDispositivo().getCambs());
            } else{
                _rdBNCambs.setChecked(true);
            }
        } else{
            _rdBPersonal.setChecked(true);
        }
        if(_servicio.getDispositivo().isConectado()){
            _rdBSRed.setChecked(true);
            _edtTMAC.setText(_servicio.getDispositivo().getMac());
            if(_servicio.getDispositivo().getIp() != null){
                _rdBIPEstatica.setChecked(true);
                _edtTIP.setText(_servicio.getDispositivo().getIp());
            } else{
                _rdBIPDinamica.setChecked(true);
            }
        } else{
            _rdBNRed.setChecked(true);
        }
        if(_servicio.getDispositivo().getIdComputadora() != 0){
            _spnRAM.setSelection(_servicio.getDispositivo().getIdRam());
            _spnSO.setSelection(_servicio.getDispositivo().getIdSO());
            _spnDD.setSelection(_servicio.getDispositivo().getIdDD());
        }
    }

    private void guardarCambios(){
        if(_chkBDispositivo.isChecked()){
            if(!_servicio.isDispositivoServicio()){
                int numDispositivo = buscarDispositivo();
                if(numDispositivo != 0){
                    _servicio.setDispositivo(numDispositivo);
                } else{
                    _servicio.setDispositivo(guardarDispositivo());
                }
            } else{
                _servicio.getDispositivo().setIdTipo(_itemSpinnerSeleccionado);
                _servicio.getDispositivo().setInventario(String.valueOf(_edtTMarca.getText()), String.valueOf(_edtTModelo.getText()), String.valueOf(_edtTNoSerie.getText()));
                if(_rdBInstitucional.isChecked()) {
                    if (!_servicio.getDispositivo().isInstitucional()) {
                        _servicio.getDispositivo().setInstitucional(true);
                    }
                    if (_rdBSCambs.isChecked()) {
                        _servicio.getDispositivo().setCambs(String.valueOf(_edtTCambs.getText()));
                    }
                }
                if(_rdBSRed.isChecked()) {
                    if (!_servicio.getDispositivo().isConectado()) {
                        _servicio.getDispositivo().setConectado(true, String.valueOf(_edtTMAC.getText()));
                    } else{
                        _servicio.getDispositivo().setMac(String.valueOf(_edtTMAC.getText()));
                    }
                    if(_rdBIPEstatica.isChecked()){
                        _servicio.getDispositivo().setIp(String.valueOf(_edtTIP.getText()));
                    }
                }
                if(_itemSpinnerSeleccionado < 3){
                    if(_servicio.getDispositivo().getIdComputadora() == 0){
                        _servicio.getDispositivo().setIdComputadora(_spnRAM.getSelectedItemPosition(), _spnDD.getSelectedItemPosition(), _spnSO.getSelectedItemPosition());
                    } else{
                        _servicio.getDispositivo().setIdRam(_spnRAM.getSelectedItemPosition());
                        _servicio.getDispositivo().setIdDD(_spnDD.getSelectedItemPosition());
                        _servicio.getDispositivo().setIdSO(_spnSO.getSelectedItemPosition());
                    }
                }
            }
        } else{
            if(_servicio.getDispositivo().getIdDispositivo() != 0){
                _servicio.removeDispositivo(this);
            }
        }
        for(int i = _evidenciasGuardadas; i < _servicio.getContadorEvidencias(); i++){
            Bitmap bitmap = ((BitmapDrawable) _imgEvidencias[i].getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageByte = baos.toByteArray();
            _servicio.setEvidencias(imageByte, i);
        }
        _servicio.setDescripcionServicio(String.valueOf(_edtTMDescripcionServicio.getText()));
    }

    private int buscarDispositivo(){
        int idElemento = 0, idDispositivo = 0;
        _db = _dbHelper.getReadableDatabase();
        if(_db != null){
            Cursor c = _db.query("inventario", null, "num_serie = ?", new String[]{String.valueOf(_edtTNoSerie.getText())}, null, null, null);
            if(c != null){
                while (c.moveToNext()){
                    idElemento = c.getInt(0);
                }
            }
            if(idElemento != 0){
                c = _db.query("dispositivos", null, "id_elemento = ?", new String[]{idElemento + ""}, null, null, null);
                if(c != null){
                    while(c.moveToNext()){
                        idDispositivo = c.getInt(0);
                    }
                }
            }
        }
        return idDispositivo;
    }

    private int guardarDispositivo(){
        int numDispositivo = 0, numElemento = 0;
        ContentValues cv = new ContentValues();
        cv.put("marca", String.valueOf(_edtTMarca.getText()));
        cv.put("modelo", String.valueOf(_edtTModelo.getText()));
        cv.put("num_serie", String.valueOf(_edtTNoSerie.getText()));
        _db = _dbHelper.getWritableDatabase();
        if(_db != null){
            numElemento = (int) _db.insert("inventario", null, cv);
            cv.clear();
            if(numElemento != -1){
                cv.put("id_elemento", numElemento);
                cv.put("id_tipo", _itemSpinnerSeleccionado);
                numDispositivo = (int) _db.insert("dispositivos", null, cv);
                cv.clear();

                cv.put("num_servicio", _numServicio);
                cv.put("id_dispositivo", numDispositivo);
                _db.insert("servicio_inventario_dispositivos", null, cv);
                cv.clear();

                if(_rdBInstitucional.isChecked()){
                    cv.put("id_dispositivo", numDispositivo);
                    if(_rdBSCambs.isChecked()){
                        cv.put("cambs", String.valueOf(_edtTCambs.getText()));
                    }
                    _db.insert("equipos_institucionales", null, cv);
                    cv.clear();
                }
                if(_rdBSRed.isChecked()){
                    cv.put("id_dispositivo", numDispositivo);
                    if(_rdBIPEstatica.isChecked()){
                        cv.put("ip", String.valueOf(_edtTIP.getText()));
                    }
                    cv.put("mac", String.valueOf(_edtTMAC.getText()));
                    _db.insert("conexiones", null, cv);
                    cv.clear();
                }
                if(_itemSpinnerSeleccionado < 3){
                    cv.put("id_elemento", numElemento);
                    cv.put("id_ram", _spnRAM.getSelectedItemPosition());
                    cv.put("id_disco_duro", _spnDD.getSelectedItemPosition());
                    cv.put("id_so", _spnSO.getSelectedItemPosition());
                    _db.insert("computadoras", null, cv);
                    cv.clear();
                }
            }
        }
        return numDispositivo;
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

        _imgEvidencias = new ImageView[3];

        _imgEvidencias[0] = findViewById(R.id.ImgVEvidencia1);
        _imgEvidencias[0].setVisibility(View.GONE);
        _imgEvidencias[1] = findViewById(R.id.ImgVEvidencia2);
        _imgEvidencias[1].setVisibility(View.GONE);
        _imgEvidencias[2] = findViewById(R.id.ImgVEvidencia3);
        _imgEvidencias[2].setVisibility(View.GONE);

        _btnTomarFoto = findViewById(R.id.BtnTomarFoto);

        //_mainBinding = ActivityCapturaBinding.inflate(getLayoutInflater());
        //setContentView(_mainBinding.getRoot());

        _imageUri = createUri();
        registerPictureLauncher();

        //_mainBinding.BtnTomarFoto.setOnClickListener(view -> {
          //  checkCameraPermission();
        //});

        _btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });

        _cnsLEvidencia = findViewById(R.id.CnsLEvidencia);
        _imgVFirma = findViewById(R.id.ImgVFirma);

        //Botones finales

        _btnFinalizar = findViewById(R.id.BtnFinalizar);
        _btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_chkBDispositivo.isChecked()) {
                    if(String.valueOf(_edtTNoSerie.getText()).equalsIgnoreCase("")){
                        Toast.makeText(Captura.this, "Complete el número de serie del dispositivo.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                guardarCambios();
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
                if(_chkBDispositivo.isChecked()) {
                    if(String.valueOf(_edtTNoSerie.getText()).equalsIgnoreCase("")){
                        Toast.makeText(Captura.this, "Complete el número de serie del dispositivo.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                guardarCambios();
                Intent intent = new Intent(getApplicationContext(), Captura.class);
                intent.putExtra("numServicio", _servicio.getNumServicio());
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkCameraPermission(){
        if(ActivityCompat.checkSelfPermission(Captura.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Captura.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else{
            _takePictureLauncher.launch(_imageUri);
        }
    }

    private void registerPictureLauncher(){
        _takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                try {
                    if(result){
                        switch (_servicio.getContadorEvidencias()){
                            case 0:;
                                _imgEvidencias[0].setVisibility(View.VISIBLE);
                                _imgEvidencias[0].setImageURI(null);
                                _imgEvidencias[0].setImageURI(_imageUri);

                                /*Bitmap bitmap = BitmapFactory.decodeFileDescriptor(getContentResolver().openFileDescriptor(_imageUri, "r").getFileDescriptor());
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);*/
                                //_imgEvidencias[0].setImageBitmap(bitmap);
                                _servicio.incrementContadorEvidencias();
                                break;
                            case 1:
                                //_mainBinding.ImgVEvidencia2.setImageURI(null);
                                //_mainBinding.ImgVEvidencia2.setImageURI(_imageUri);
                                _imgEvidencias[1].setVisibility(View.VISIBLE);
                                _imgEvidencias[1].setImageURI(null);
                                _imgEvidencias[1].setImageURI(_imageUri);
                                _servicio.incrementContadorEvidencias();
                                break;
                            case 2:
                                _imgEvidencias[2].setVisibility(View.VISIBLE);
                                _imgEvidencias[2].setImageURI(null);
                                _imgEvidencias[2].setImageURI(_imageUri);

                                //_mainBinding.ImgVEvidencia3.setImageURI(null);
                                //_mainBinding.ImgVEvidencia3.setImageURI(_imageUri);
                                _servicio.incrementContadorEvidencias();
                                break;
                            case 3:
                                Toast.makeText(Captura.this, "El límite de fotos para evidencias es 3", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(Captura.this, _servicio.getContadorEvidencias(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(Captura.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Uri createUri(){
        File imageFile = new File(getApplicationContext().getFilesDir(), "camera_photo.jpg");
        return FileProvider.getUriForFile(getApplicationContext(), "hojasservicio.fileProvider", imageFile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                _takePictureLauncher.launch(_imageUri);
            } else{
                Toast.makeText(this, "No se dio permiso de usar la cámara.", Toast.LENGTH_SHORT).show();
            }
        }
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
}
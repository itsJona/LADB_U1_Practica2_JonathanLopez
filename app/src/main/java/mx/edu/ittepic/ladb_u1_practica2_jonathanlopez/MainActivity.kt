package mx.edu.ittepic.ladb_u1_practica2_jonathanlopez

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button3.setOnClickListener {
            if(editText5.text.isEmpty()) {
                Toast.makeText(this, "Ingrese un nombre de archivo para guardar", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(radioButton2.isChecked){
                guardarInterno()
            }
            if(radioButton3.isChecked){
                if(ContextCompat.checkSelfPermission(this,     //context es estática  --- check
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    //SI ENTRA ENTONCES AUN NO SE HAN OTORGADO PERMISOS
                    //EL SIGUIENTE CODIGO LOS SOLICITA
                    ActivityCompat.requestPermissions(this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),0)

                }else{
                    guardarSD()
                }

            }
        }
        button4.setOnClickListener {
            if(editText5.text.isEmpty()){
                Toast.makeText( this,"Ingrese un nombre de archivo para abrir",Toast.LENGTH_LONG).show()
            return@setOnClickListener}

            if(radioButton2.isChecked){
                leerInterno()

            }
            if(radioButton3.isChecked){
                if(ContextCompat.checkSelfPermission(this,     //context es estática  --- check
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    //SI ENTRA ENTONCES AUN NO SE HAN OTORGADO PERMISOS
                    //EL SIGUIENTE CODIGO LOS SOLICITA
                    ActivityCompat.requestPermissions(this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),0)

                }else{
                    leerSD()
                }

            }
        }
    }//over


        fun noSD(): Boolean{
            var estado = Environment.getExternalStorageState()
            if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }
        fun guardarInterno(){

            try {
                var flujoSalida=OutputStreamWriter(openFileOutput(editText5.text.toString(),
                    Context.MODE_PRIVATE))

                flujoSalida.write(editText4.text.toString())
                flujoSalida.flush()
                flujoSalida.close()

                mensaje("Archivo guardado en memorita interna")

                editText4.setText("")

            }catch (error: IOException){
                mensaje(error.message.toString())
            }
    }
    fun guardarSD(){

        if(noSD()){
            mensaje("No hay memoria externa")
            return
        }
        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,editText5.text.toString())
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))


            flujoSalida.write(editText4.text.toString())
            flujoSalida.flush()    //commit
            flujoSalida.close()

            mensaje("Archivo guardado en memorita SD")

            editText4.setText("")

        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }


    fun leerSD(){
        if(noSD()){
            mensaje("Ni hay memoria externa")
            return
        }
        try {
            var rutaSD=Environment.getExternalStorageDirectory()
            var datosArchivo= File(rutaSD.absolutePath,editText5.text.toString())

            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data= flujoEntrada.readLine()//.split("&")
            editText4.setText(data)
            flujoEntrada.close()

        }catch (error:IOException){
            mensaje(error.message.toString())
        }
    }

    fun leerInterno(){
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(editText5.text.toString())))
            var data=flujoEntrada.readLine()
            editText4.setText(data)
        }
        catch (error:IOException){
            mensaje(error.message.toString())
        }

    }
    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("OK"){d,i-> }
            .show()
    }



}

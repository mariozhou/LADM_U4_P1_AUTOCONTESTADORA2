package mx.tecnm.tepic.ladm_u4_p1_autocontestadora

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    var baseFirebase= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btncontacto = findViewById<Button>(R.id.btncontacto)
        val btnmsn = findViewById<Button>(R.id.btnmsn)
        val btnmsn2 = findViewById<Button>(R.id.btnmsn2)

        asignarAccesos()

        btncontacto.setOnClickListener {
            agregarContacto()
        }

        btnmsn.setOnClickListener {
            agregarMsn()


        }

        btnmsn2.setOnClickListener {
            agregarMsnoagra()
        }

        }

    private fun agregarMsnoagra() {
        val mnsnoagradable = findViewById<EditText>(R.id.mnsnoagradable)
        var mns = ""
        baseFirebase.collection("Mensajes")
            .whereEqualTo("Agradable", false)
            .get()
            .addOnSuccessListener { documents ->
                mns = ""
                for (document in documents) {
                    mns =  "${document.id}"
                }
                baseFirebase.collection("Mensajes")
                    .document("${mns}")
                    .update("Mensaje", mnsnoagradable.text.toString())
                mnsnoagradable.setText("")
            }

    }

    private fun agregarMsn() {
        val mnsagradable = findViewById<EditText>(R.id.mnsagradable)
        var mns = ""
        baseFirebase.collection("Mensajes")
            .whereEqualTo("Agradable", true)
            .get()
            .addOnSuccessListener { documents ->
                mns = ""
                for (document in documents) {
                    mns =  "${document.id}"
                }
                baseFirebase.collection("Mensajes")
                    .document("${mns}")
                    .update("Mensaje", mnsagradable.text.toString())
                mnsagradable.setText("")
            }

    }

    private fun asignarAccesos() {
        val permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 1)
        }
         if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),1)
         }
          if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1){

        }
    }

    private fun agregarContacto(){
       val ednombre = findViewById<EditText>(R.id.ednombre)
        val ednumero = findViewById<EditText>(R.id.ednumero)
        val RAgradable = findViewById<RadioButton>(R.id.RAgradable)
        val RNoAgradable = findViewById<RadioButton>(R.id.RNoAgradable)

        if(RAgradable.isChecked ){
            var insertar = hashMapOf(
                "Nombre" to ednombre.text.toString(),
                "Celular" to ednumero.text.toString(),
                "Agradable" to true
            )
            baseFirebase.collection("Contactos")
                .add(insertar)
        }
        if(RNoAgradable.isChecked){
            var insertar = hashMapOf(
                "Nombre" to ednombre.text.toString(),
                "Celular" to ednumero.text.toString(),
                "Agradable" to false
            )
            baseFirebase.collection("Contactos")
                .add(insertar)
        }
        limpiarcontacto()
    }

    private fun limpiarcontacto() {
        val ednombre = findViewById<EditText>(R.id.ednombre)
        val ednumero = findViewById<EditText>(R.id.ednumero)

       ednombre.setText("")
        ednumero.setText("")
    }

}

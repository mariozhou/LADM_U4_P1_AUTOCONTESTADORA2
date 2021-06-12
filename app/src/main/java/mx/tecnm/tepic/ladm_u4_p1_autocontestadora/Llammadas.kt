package mx.tecnm.tepic.ladm_u4_p1_autocontestadora

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Llammadas :BroadcastReceiver(){
    var baseFirebase= FirebaseFirestore.getInstance()
    private var phoenManager: TelephonyManager? = null
    var isagrad=true
    var status=false
    var msn = ""


    override fun onReceive(context: Context, intent: Intent?) {
        phoenManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val listenerTelefono: PhoneStateListener = object : PhoneStateListener() {

            override fun onCallStateChanged(state: Int, numero: String){
                super.onCallStateChanged(state, numero)
                status=false
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING-> {

                        baseFirebase.collection("Contactos")
                            .whereEqualTo("Celular", numero)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    isagrad = document.getBoolean("Agradable")!!
                                }
                                if (isagrad) {
                                    msn = "a"
                                    baseFirebase.collection("Mensajes")
                                        .whereEqualTo("Agradable", true)
                                        .get()
                                        .addOnSuccessListener { documents ->
                                            for (document in documents) {
                                                msn= "${document.get("Mensaje")}"
                                            }
                                            SmsManager.getDefault().sendTextMessage(numero,
                                                null, msn, null, null)
                                            Toast.makeText(context, "Mensaje Enviado Agradable: \n"
                                                  +  "[${msn}] \n", Toast.LENGTH_LONG)
                                                .show()
                                        }
                                }
                                if (!isagrad) {
                                    baseFirebase.collection("Mensajes")
                                        .whereEqualTo("Agradable", false)
                                        .get()
                                        .addOnSuccessListener { documents ->
                                            for (document in documents) {
                                                msn= "${document.get("Mensaje")}"
                                            }

                                            SmsManager.getDefault().sendTextMessage(numero,
                                                null, msn, null, null)
                                            Toast.makeText(context, "Mensaje Enviado No agradable \n"
                                                    + "[${msn}] \n"
                                                    , Toast.LENGTH_LONG)
                                                .show()
                                        }
                                }
                            }

                    }
                }
            }

        }


        if (!isListening) {
            phoenManager!!.listen(listenerTelefono, PhoneStateListener.LISTEN_CALL_STATE)
            isListening = true
        }
    }
    companion object {
        var isListening = false
    }


}
package com.rosinante24.firebasefirestoreandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_satu)
    EditText editTextSatu;
    @BindView(R.id.edit_text_dua)
    EditText editTextDua;
    @BindView(R.id.button_save)
    Button buttonSave;
    @BindView(R.id.text_result)
    TextView textResult;

    private String satu;
    private String dua;

    private DocumentReference documentReference = FirebaseFirestore.getInstance().document("sampleData/randomLah");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_save)
    public void onClick() {
        satu = editTextSatu.getText().toString();
        dua = editTextDua.getText().toString();

        if (satu.isEmpty() || dua.isEmpty()) {
            return;
        }

        Map<String, Object> dataToSave = new HashMap<String, Object>();

        dataToSave.put(getString(R.string.TEXT_SATU), satu);
        dataToSave.put(getString(R.string.TEXT_DUA), dua);

        documentReference
                .set(dataToSave)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(MainActivity.this, "Berhasil Bang!", Toast.LENGTH_SHORT).show();

//                        kalo mau hemat method, sama baris, make if-else ajh :v
//
//                        if (task.isSuccessful()){
//                            Toast.makeText(MainActivity.this, "Berhasil Bang!", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Toast.makeText(MainActivity.this, "Gagal Bang!", Toast.LENGTH_SHORT).show();
//                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "Gagal Bang!", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    // method untuuk menentukan action saat aplikasi dijalankan
    @Override
    protected void onStart() {
        super.onStart();

//         untuk mengambil data, tapi gk langsung muncul

//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                if (documentSnapshot.exists()) {
////                    DataModel dataModel = documentSnapshot.toObject(DataModel.class);
//                  String satu = documentSnapshot.getString("satu");
//                  String  dua = documentSnapshot.getString("dua");
//
//                    textResult.setText("Result : " + satu + "," + dua);
//                } else {
//                    Toast.makeText(MainActivity.this, "Gagal coeg", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//      untuk mengambil data, dan langsung muncul
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (documentSnapshot.exists()) {
                    String satu = documentSnapshot.getString(getResources().getString(R.string.TEXT_SATU));
                    String dua = documentSnapshot.getString(getResources().getString(R.string.TEXT_DUA));
                    textResult.setText("Result : " + satu + ", " + dua);
                } else if (e != null) {
                    Toast.makeText(MainActivity.this, "Gagal!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}

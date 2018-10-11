package com.eusecom.samshopersung;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import android.security.KeyPairGeneratorSpec;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.security.keystore.KeyProtection;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eusecom.samshopersung.soap.EncodeSignatureTools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.security.auth.x500.X500Principal;

import org.apache.commons.io.FileUtils;

//https://www.androidauthority.com/use-android-keystore-store-passwords-sensitive-information-623779/
//https://github.com/obaro/SimpleKeystoreApp

public class KeyStoreActivity extends AppCompatActivity {

    static final String TAG = "SimpleKeystoreApp";
    static final String CIPHER_TYPE = "RSA/ECB/PKCS1Padding";
    static final String CIPHER_PROVIDER = "AndroidOpenSSL";

    EditText aliasText;
    EditText startText, decryptedText, encryptedText;
    List<String> keyAliases;
    ListView listView;
    KeyRecyclerAdapter listAdapter;

    KeyStore keyStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception e) {
        }
        refreshKeys();

        setContentView(R.layout.activity_keystore);

        View listHeader = View.inflate(this, R.layout.activity_keystore_header, null);
        aliasText = (EditText) listHeader.findViewById(R.id.aliasText);
        startText = (EditText) listHeader.findViewById(R.id.startText);
        decryptedText = (EditText) listHeader.findViewById(R.id.decryptedText);
        encryptedText = (EditText) listHeader.findViewById(R.id.encryptedText);

        //encryptedText.setText("WMtKrhUp0OW0XOdF0WsAisTKO0HjK22q/ao7T8dWw5ePCe3dXLm/Ktne2DiO/6nDC7jNt51zxaDiPrRQFPaoz5L3qU1nlaITFRkh4Qagi3pCsHBA1v92ug1lNw3j59xcHgQlgIcEB6QbJro+LyOWdpTirqe1TYbUrKo+suAATvo4yzup77uXWFl1WGT0IH211/i+m8hyY+931QDHRL4lduObTMJLyzq7zUMKaiXTzBz11/x5Yqn/ydVkCDIaxtvTDnXCBSW6ccMWj8O2NmSEGO1ChEbFdcjfB0kgviomOpzhyL2eURiudGZTrDaPWdGnLYth5McE/1VYHp82dalktw==");
        startText.setText("2004567890|99920045678900001|1|2018-06-27T14:34:14+02:00|237.23");


        listView = (ListView) findViewById(R.id.listView);
        listView.addHeaderView(listHeader);
        listAdapter = new KeyRecyclerAdapter(this, R.id.keyAlias);
        listView.setAdapter(listAdapter);
    }

    private void refreshKeys() {
        keyAliases = new ArrayList<>();
        try {
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                keyAliases.add(aliases.nextElement());
            }
        } catch (Exception e) {
        }

        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();
    }

    public void createNewKeys(View view) {
        String alias = aliasText.getText().toString();
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 25);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);

                KeyPair keyPair = generator.generateKeyPair();

                PublicKey publicKey = keyPair.getPublic();
                byte[] publicKeyBytes = Base64.encode(publicKey.getEncoded(), 0);
                String pubKey = new String(publicKeyBytes);
                Log.d("encrypt Public key ", pubKey);

                //i can not get private key
                //PrivateKey privateKey = keyPair.getPrivate();
                //byte[] privateKeyBytes = Base64.encode(privateKey.getEncoded(),0);
                //String privKey = new String(Base64.encode(privateKey.getEncoded(),0));
                //Log.d("encrypt Private key ", privKey);


            } else {
                Toast.makeText(this, "Alias " + alias + " allready exists", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        refreshKeys();
    }

    public void deleteKey(final String alias) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Delete Key")
                .setMessage("Do you want to delete the key \"" + alias + "\" from the keystore?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            keyStore.deleteEntry(alias);
                            refreshKeys();
                        } catch (KeyStoreException e) {
                            Toast.makeText(KeyStoreActivity.this,
                                    "Exception " + e.getMessage() + " occured",
                                    Toast.LENGTH_LONG).show();
                            Log.e(TAG, Log.getStackTraceString(e));
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    public void encryptStringWithPublicKey(String alias) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            byte[] publicKeyBytes = Base64.encode(publicKey.getEncoded(), 0);
            String pubKey = new String(publicKeyBytes);
            Log.d("encrypt Public key ", pubKey);


            String initialText = startText.getText().toString();
            if (initialText.isEmpty()) {
                Toast.makeText(this, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
                return;
            }

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();
            encryptedText.setText(Base64.encodeToString(vals, Base64.DEFAULT));
            Log.d("encrypted ", Base64.encodeToString(vals, Base64.DEFAULT));

        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void encryptStringWithPrivateKey(String alias) {
        try {

            Log.d("alias", alias);
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);

            String initialText = startText.getText().toString();
            if (initialText.isEmpty()) {
                Toast.makeText(this, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
                return;
            }

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getPrivateKey());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();
            encryptedText.setText(Base64.encodeToString(vals, Base64.DEFAULT));
            Log.d("encrypted ", Base64.encodeToString(vals, Base64.DEFAULT));

        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public byte[] signature;

    public void createSignatureWithPrivateKey(String alias) {
        try {
            byte[] vals = getSignature(startText.getText().toString(), getPrivateKeyFromKeyStore(alias));

            signature = vals;
            encryptedText.setText(Base64.encodeToString(vals, Base64.DEFAULT));
            Log.d("signature ", Base64.encodeToString(vals, Base64.DEFAULT));
        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }


    }

    public void verifySignaturegWithPublicKey(String alias) {

        boolean verified = verifySignature(startText.getText().toString(), signature, getPublicKeyFromKeyStore(alias));

        if (verified == true) {
            decryptedText.setText("Signature is verified");
        } else {
            decryptedText.setText("Signature is NOT verified");
        }

    }

    public void decryptStringWithPublicKey(String alias) {
        try {

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            byte[] publicKeyBytes = Base64.encode(publicKey.getEncoded(), 0);
            String pubKey = new String(publicKeyBytes);
            Log.d("decrypt with Pub key", pubKey);


            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            output.init(Cipher.DECRYPT_MODE, publicKey);

            String cipherText = encryptedText.getText().toString();
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            decryptedText.setText(finalText);

        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void decryptStringWithPrivateKey(String alias) {
        try {

            //only Android Lolipo und under
            //KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            //RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
            //Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            //output.init(Cipher.DECRYPT_MODE, privateKey);

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);

            //i can not write out private key
            //byte[] privateKeyBytes = Base64.encode(privateKeyEntry.getPrivateKey().getEncoded(),0);
            //String privKey = new String(privateKeyBytes);
            //Log.d("encrypt Private key ", privKey);

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            String cipherText = encryptedText.getText().toString();
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            decryptedText.setText(finalText);

        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public class KeyRecyclerAdapter extends ArrayAdapter<String> {

        public KeyRecyclerAdapter(Context context, int textView) {
            super(context, textView);
        }

        @Override
        public int getCount() {
            return keyAliases.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_itemkeystore, parent, false);

            final TextView keyAlias = (TextView) itemView.findViewById(R.id.keyAlias);
            keyAlias.setText(keyAliases.get(position));

            Button encryptButton = (Button) itemView.findViewById(R.id.encryptButton);
            encryptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    encryptStringWithPrivateKey(keyAlias.getText().toString());
                }
            });
            Button decryptButton = (Button) itemView.findViewById(R.id.decryptButton);
            decryptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    decryptStringWithPublicKey(keyAlias.getText().toString());
                }
            });
            Button encryptButton2 = (Button) itemView.findViewById(R.id.encryptButton2);
            encryptButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    encryptStringWithPublicKey(keyAlias.getText().toString());

                }
            });
            Button decryptButton2 = (Button) itemView.findViewById(R.id.decryptButton2);
            decryptButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    decryptStringWithPrivateKey(keyAlias.getText().toString());
                }
            });

            Button encryptButton3 = (Button) itemView.findViewById(R.id.encryptButton3);
            encryptButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    createSignatureWithPrivateKey(keyAlias.getText().toString());

                }
            });
            Button decryptButton3 = (Button) itemView.findViewById(R.id.decryptButton3);
            decryptButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    verifySignaturegWithPublicKey(keyAlias.getText().toString());
                }
            });

            final Button deleteButton = (Button) itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteKey(keyAlias.getText().toString());

                }
            });

            final Button importButton = (Button) itemView.findViewById(R.id.importButton);
            importButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setCertificateToKeyStore();
                }
            });

            return itemView;
        }

        @Override
        public String getItem(int position) {
            return keyAliases.get(position);
        }

    }

    public static byte[] getSignature(String textMessage, PrivateKey privateKey) {

        try {
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(privateKey);
            signer.update(textMessage.getBytes("UTF-8"));
            byte[] signature = signer.sign();

            return signature;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static PrivateKey getPrivateKeyFromKeyStore(String alias) {

        KeyStore keyStore = null;
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            return privateKeyEntry.getPrivateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean verifySignature(String textMessage, byte[] signature, RSAPublicKey publicKey) {

        boolean success = false;

        try {

            Log.d("signature ", Base64.encodeToString(signature, Base64.DEFAULT));

            Signature s = Signature.getInstance("SHA256withRSA");
            s.initVerify(publicKey);

            s.update(textMessage.getBytes("UTF-8"));

            success = s.verify(signature);

            return success;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static RSAPublicKey getPublicKeyFromKeyStore(String alias) {

        KeyStore keyStore = null;
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            byte[] publicKeyBytes = Base64.encode(publicKey.getEncoded(), 0);
            String pubKey = new String(publicKeyBytes);
            Log.d("decrypt with Pub key", pubKey);

            return publicKey;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public static void setCertificateToKeyStore() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("more than M", "");
            try {
                //by https://developer.android.com/reference/android/security/keystore/KeyProtection

                // RSA private key
                PrivateKey privateKey = EncodeSignatureTools.getPrivateKeyFromString(Constants.CAROL_PRIVATE_KEY);
                // Certificate chain with the first certificate containing the corresponding RSA public key.
                CertificateFactory certificateFactory = null;
                try {
                    certificateFactory = CertificateFactory.getInstance("X.509");
                } catch (CertificateException e) {
                    Log.wtf(TAG, e);
                }

                //ByteArrayInputStream bais = getByteFromFile();

                Certificate[] certChain = {certificateFactory.generateCertificate(getByteFromFile())};

                KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);

                keyStore.setEntry(
                        "key3",
                        new KeyStore.PrivateKeyEntry(privateKey, certChain),
                        new KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
                                | KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512)
                                .build());

                // Key pair imported

                //obtain a reference to it.
                //PrivateKey keyStorePrivateKey = (PrivateKey) keyStore.getKey("key2", null);
                //PublicKey publicKey = keyStore.getCertificate("key2").getPublicKey();
                // The original private key can now be discarded.

                //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                //cipher.init(Cipher.DECRYPT_MODE, keyStorePrivateKey);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }

        }

    }

    public static ByteArrayInputStream getByteFromFile() {

        StringBuilder sb = new StringBuilder();

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "/eusecom/carol.crt";
        File file = new File(baseDir + File.separator + fileName);

        try {
            String str = FileUtils.readFileToString(file);
            System.out.println(str);
            return new ByteArrayInputStream(str.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}

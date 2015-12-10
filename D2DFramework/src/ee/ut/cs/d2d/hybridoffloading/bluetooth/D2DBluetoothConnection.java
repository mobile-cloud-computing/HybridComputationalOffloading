package ee.ut.cs.d2d.hybridoffloading.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;

import ee.ut.cs.d2d.hybridoffloading.Pack;
import ee.ut.cs.d2d.hybridoffloading.ResultPack;

/**
 * Created by hflores on 09/12/15.
 */
public class D2DBluetoothConnection implements Runnable {

    private final String TAG = D2DBluetoothConnection.class.getSimpleName();

    InputStream in = null;
    OutputStream out = null;

    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;

    long processTime;

    BluetoothSocket mysocket = null;

    public D2DBluetoothConnection(BluetoothSocket mysocket){
        this.mysocket = mysocket;

    }

    public void makeconnection(){

        try {
            in =  mysocket.getInputStream();
            out = mysocket.getOutputStream();


            oos = new ObjectOutputStream(out);
            //oos = new ObjectOutputStream(new BufferedOutputStream(out));
            oos.flush();

            ois = new ObjectInputStream(in);
            //ois =new ObjectInputStream(new BufferedInputStream(in));

            System.out.println("connection established");


            waitforreceivingdata();

        } catch (SocketException ex) {
            Log.d(TAG, "Hubo error makeconnection 1");

        } catch (IOException ex) {
            Log.d(TAG, "Hubo error makeconnection 2");
        } catch (Exception ex) {
            Log.d(TAG, "Hubo error makeconnection 3");
        }
    }

    private void waitforreceivingdata() {
        try {
            new Receiving().waitforreceivingdata();
        } catch (Exception ex) {
            Log.d(TAG, "Hubo error makeconnection 4");
        }
    }

    @Override
    public void run() {
        makeconnection();
    }


    class Receiving implements Runnable {
        String functionName = null;
        Class[] paramTypes = null;
        Object[] paramValues = null;
        Object state = null;
        Class stateDType = null;
        Pack myPack = null;

        //List<String> timestamps;

        public Receiving() {
        }

        public void waitforreceivingdata() {
            Thread t = new Thread(this);
            System.out.println("Thread Starting ");
            t.start();
        }

        @Override
        public void run() {
            try {

                processTime = System.currentTimeMillis();
                myPack = (Pack) ois.readObject();
                functionName = myPack.getfunctionName();
                paramTypes = myPack.getparamTypes();
                paramValues = myPack.getparamValues();
                state = myPack.getstate();
                stateDType = myPack.getstateType();
                //timestamps = myPack.getTimeStamps();


                if (functionName != null && functionName.length() > 0) {
                    try {

                        Log.d(TAG, "trying to load and execute");
                        Class cls = Class.forName(stateDType.getName());

                        //timestamps.add(processTime+",server1");

                        /*System.out.println(""+stateDType.getName());

                        System.out.println("functionsName: " + functionName.toLowerCase());
                        System.out.println("paramTypes: " + paramTypes.toString());*/

                        Method method = cls.getDeclaredMethod(functionName, paramTypes);



                        try{

                            /**
                             * support for caching results
                             */


                            /**
                             * cache using the parameters as key of the result can be possible
                             */
                            //boolean processed = TreeManager.getInstance().exists(paramValues[0]);

                            //System.out.println("param1:" + (int[]) paramValues[0]);
                            //System.out.println("param1:" + paramValues[1]);
                            //System.out.println("param1:" +  paramValues[2]);


                            oos.flush();
                            Object result = method.invoke(state, paramValues);
                            ResultPack rp = new ResultPack(result, state);
                            //rp.setTimeStamps(timestamps);

                            //System.out.println("Size in bytes: " + sizeInBytes(rp));
                            oos.flush();
                            oos.writeObject(rp);
                            //System.out.println("Object wrote it");
                            oos.flush();
                            //System.out.println("Object executed and flushed: " + (System.currentTimeMillis() - processTime));

                            Log.d(TAG, "Object executed and flushed:");



                            //System.out.println("Object executed and flushed: " + (System.currentTimeMillis() - processTime));



                        } catch (IllegalAccessException ex1) {
                            returnnull(oos);
                            System.out.println("Hubo problema 1: " + ex1.getMessage());
                        } catch (InvocationTargetException ex2) {
                            returnnull(oos);
                            System.out.println("Hubo problema 2: "+ ex2.getCause());
                        } catch(Exception ex3){
                            ResultPack rp = new ResultPack(null, state);
                            oos.writeObject(rp);
                            oos.flush();
                            System.out.println("Hubo problema 3" + ex3.getCause());
                        }

                    } catch (ClassNotFoundException ex) {
                        returnnull(oos);
                        System.out.println("Hubo problema 4");
                    }  catch (IllegalArgumentException ex) {
                        returnnull(oos);
                        System.out.println("Hubo problema 5");
                    } catch (NoSuchMethodException ex) {
                        returnnull(oos);
                        System.out.println("Hubo problema 6");
                    } catch (SecurityException ex) {
                        returnnull(oos);
                        System.out.println("Hubo problema 7");
                    } finally {



                        oos.close();
                        ois.close();

                        in.close();
                        out.close();

                        mysocket.close();

                        oos = null;
                        ois = null;

                        in = null;
                        out = null;
                        mysocket = null;

                    }
                } else {
                    returnnull(oos);
                }
            } catch (IOException ex) {
                returnnull(oos);
                System.out.println("Hubo problema 8");
                //System.out.println(ex.getCause() + "problema" + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                returnnull(oos);
                System.out.println("Hubo problema 9");
                ex.printStackTrace();
            } /*finally {
                makeconnection();
            }*/
        }
    }

    void returnnull(ObjectOutputStream oos){
        if(oos != null)
            try {
                oos.writeObject(null);
                oos.flush();
            } catch (IOException ex1) {

            }
    }

    public static int sizeInBytes(Object obj) throws IOException
    {
        ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        byteObject.close();

        return byteObject.toByteArray().length;
    }
}

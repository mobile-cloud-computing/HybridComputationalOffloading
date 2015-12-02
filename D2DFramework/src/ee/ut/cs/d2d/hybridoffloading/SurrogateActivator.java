/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/

package ee.ut.cs.d2d.hybridoffloading;


import android.os.AsyncTask;

public class SurrogateActivator {

    /*public static void main(String[] args) {
        NetworkManagerServer nm = new NetworkManagerServer(NetInfo.port);
        nm.makeconnection();
    }*/

    public SurrogateActivator(){

    }

    public void initiate(){
        new ListenInBackground().execute("");
    }

    private class ListenInBackground extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            NetworkManagerServer nm = new NetworkManagerServer(NetInfo.port);
            nm.makeconnection();

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}

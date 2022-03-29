package buidcopgrs.in.buidco.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;

import buidcopgrs.in.buidco.Utilitites.Utiilties;
import buidcopgrs.in.buidco.database.WebServiceHelper;
import buidcopgrs.in.buidco.entity.GrivanceUserWiseData;
import buidcopgrs.in.buidco.interfaces.GrivanceRoleWiseBinder;

public class GetGrivanceRoleWiseService extends AsyncTask<String,Void, ArrayList<GrivanceUserWiseData>> {
    static GrivanceRoleWiseBinder grivanceRoleWiseBinder;
    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public GetGrivanceRoleWiseService(Activity activity){
       this.activity=activity;
        dialog1= new ProgressDialog(this.activity);
        alertDialog=new AlertDialog.Builder(this.activity).create();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog1.setMessage("Loading Grivance...");
        dialog1.setCancelable(false);
        dialog1.show();
    }

    @Override
    protected ArrayList<GrivanceUserWiseData> doInBackground(String... strings) {
        ArrayList<GrivanceUserWiseData> result = null;
        if (Utiilties.isOnline(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                result = WebServiceHelper.getGrivanceRoleWise(strings);

            }else{
                alertDialog.setMessage("Your device must have atleast Kitkat or Above Version");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }else{
            Toast.makeText(activity, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<GrivanceUserWiseData> res) {
        super.onPostExecute(res);
        //SUCCESS Your User Id is Mobile No and Password :- Gr@ba993123
        if (dialog1.isShowing())dialog1.dismiss();
        if (res==null){
            alertDialog.setMessage("Something went wrong ! Please trurn on internet connection !");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }else{
           if (res.size()>0) {
               grivanceRoleWiseBinder.rolegrivanceLoaded(res);
           }
           else{
               Toast.makeText(activity, "Grivance Not Found !", Toast.LENGTH_SHORT).show();
               grivanceRoleWiseBinder.grivanceNotFound();
           }
        }
    }
    public static void bindGTListener(GrivanceRoleWiseBinder listener) {
        grivanceRoleWiseBinder = listener;
    }
}

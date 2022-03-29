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
import buidcopgrs.in.buidco.entity.UserByLocData;
import buidcopgrs.in.buidco.interfaces.UserLocBinder;

public class GetUserLocationWIseService extends AsyncTask<String,Void, ArrayList<UserByLocData>> {
    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;
    static UserLocBinder userLocBinder;
    public GetUserLocationWIseService(Activity activity){
       this.activity=activity;
        dialog1= new ProgressDialog(this.activity);
        alertDialog=new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog1.setMessage("Get User By Location...");
        dialog1.setCancelable(false);
        dialog1.show();
    }

    @Override
    protected ArrayList<UserByLocData> doInBackground(String... strings) {
        ArrayList<UserByLocData> result = null;
        if (Utiilties.isOnline(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                result = WebServiceHelper.getUserByLocation(strings);
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
    protected void onPostExecute(ArrayList<UserByLocData> res) {
        super.onPostExecute(res);
        if (dialog1.isShowing())dialog1.dismiss();
        if (res==null){
            alertDialog.setMessage("Something went wrong ! When Loading District !");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }else{
            if (res.size()>0){
                userLocBinder.bindUserLoc(res);
            }else{
                userLocBinder.noDataFound();
            }
        }
    }
   public static void bindUserLocData(UserLocBinder userLocBinder1){
       userLocBinder=userLocBinder1;
   }

}

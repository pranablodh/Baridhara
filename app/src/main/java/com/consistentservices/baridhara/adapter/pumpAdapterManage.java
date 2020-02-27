package com.consistentservices.baridhara.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.consistentservices.baridhara.R;
import com.consistentservices.baridhara.adapterDataBinder.pumpDataManage;
import com.consistentservices.baridhara.networkFunction.url;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pumpAdapterManage extends RecyclerView.Adapter<pumpAdapterManage.pumpManagerViewHolder>
{
    private Context mCtx;
    private List<pumpDataManage> pumpListManage;

    //APIs
    private String DELETE_URL = String.format("%s%s", url.BASE_URL, url.SUFFIX_DELETE_PUMP);
    private String RENAME_URL = String.format("%s%s", url.BASE_URL, url.SUFFIX_RENAME_PUMP);
    private String ADD_USER_URL = String.format("%s%s", url.BASE_URL, url.SUFFIX_ADD_ANOTHER_USER);
    private String REMOVE_USER_URL = String.format("%s%s", url.BASE_URL, url.SUFFIX_REMOVE_ANOTHER_USER);
    private String TOKEN = "";

    //Variables for message dialog
    private Dialog messageDialog;
    private Dialog deleteDialog;
    private Dialog renameDialog;
    private Dialog addUserDialog;
    private Dialog removeUserDialog;

    //TextView and EditText For Dialog
    private TextView message;
    private TextView header_delete;
    private TextView header_rename;
    private TextView header_addUser;
    private TextView header_removeUser;
    private EditText rename_text;
    private EditText user_name_text;
    private EditText user_mobile;

    //Buttons for Dialog
    private Button yes_button;
    private Button no_button;
    private Button okay_Remove_user;
    private Button okay_green;
    private Button okay_red;
    private Button ok_rename;
    private Button cancel_rename;
    private Button ok_addUser;
    private Button cancel_addUser;

    //System Functions
    public pumpAdapterManage(Context mCtx, List<pumpDataManage> pumpList)
    {
        this.mCtx = mCtx;
        this.pumpListManage = pumpList;
    }

    //System Functions
    @NonNull
    @Override
    public pumpManagerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_layout_manage_pump, null);
        return new pumpManagerViewHolder(view);
    }

    //System Functions
    @Override
    public void onBindViewHolder(@NonNull final pumpManagerViewHolder pumpManagerViewHolder, int i)
    {
        pumpDataManage pDataManage = pumpListManage.get(i);

        pumpManagerViewHolder.name.setText(pDataManage.getName());
        pumpManagerViewHolder.id.setText(pDataManage.getID());

        //Dialog Initialization
        deleteDialog = new Dialog(mCtx);
        messageDialog = new Dialog(mCtx);
        renameDialog = new Dialog(mCtx);
        addUserDialog = new Dialog(mCtx);
        removeUserDialog = new Dialog(mCtx);

        final int currentPosition = i;

        //On Click Listener For Our CardView Items
        pumpManagerViewHolder.add_user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addUserToPump(currentPosition, pumpManagerViewHolder.name.getText().toString());
            }
        });

        //On Click Listener For Our CardView Items
        pumpManagerViewHolder.remove_user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                removeUserFromPump(currentPosition, pumpManagerViewHolder.name.getText().toString());
            }
        });

        //On Click Listener For Our CardView Items
        pumpManagerViewHolder.rename.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                renameData(currentPosition, pumpManagerViewHolder.name.getText().toString());
            }
        });

        //On Click Listener For Our CardView Items
        pumpManagerViewHolder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deleteData(currentPosition, pumpManagerViewHolder.name.getText().toString());
            }
        });
    }

    //System Functions
    @Override
    public int getItemCount()
    {
        return pumpListManage.size();
    }

    //System Functions
    class pumpManagerViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView id;
        Button remove_user;
        Button add_user;
        Button rename;
        Button delete;

        public pumpManagerViewHolder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.id);
            remove_user = itemView.findViewById(R.id.remove_user);
            add_user = itemView.findViewById(R.id.add_user);
            rename = itemView.findViewById(R.id.rename);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    //DELETE DEVICE
    public void deleteData(final int position, final String name)
    {
        deleteDialog.setContentView(R.layout.dialog_delete);
        deleteDialog.setCancelable(false);
        header_delete = (TextView) deleteDialog.findViewById(R.id.header_delete);
        yes_button = (Button) deleteDialog.findViewById(R.id.yes_button);
        no_button =  (Button) deleteDialog.findViewById(R.id.no_button);
        header_delete.setText("Delete: "+name+"?");
        yes_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int deleteResponse = requestOperation(name,DELETE_URL);
                deleteDialog.dismiss();
            }
        });

        no_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.show();
    }

    //Rename Device
    public void renameData(int position, final String name)
    {
        renameDialog.setContentView(R.layout.dialog_rename);
        renameDialog.setCancelable(false);
        header_rename = (TextView) renameDialog.findViewById(R.id.header_rename);
        rename_text = (EditText) renameDialog.findViewById(R.id.rename_text) ;
        ok_rename = (Button) renameDialog.findViewById(R.id.ok_rename);
        cancel_rename =  (Button) renameDialog.findViewById(R.id.cancel_rename);
        header_rename.setText("Rename: "+name);
        ok_rename.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(String.valueOf(rename_text.getText()).isEmpty())
                {
                    rename_text.setError("Field can't be empty");
                    return;
                }

                if(rename_text.getText().toString().trim().length() == 0)
                {
                    rename_text.setError("Field can't be empty");
                    return;
                }
                Toast.makeText(mCtx,rename_text.getText().toString(),Toast.LENGTH_SHORT).show();
                renameDialog.dismiss();
            }
        });

        cancel_rename.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                renameDialog.dismiss();
            }
        });
        renameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        renameDialog.show();
    }

    //Add User To Pump
    public void addUserToPump(int position, final String name)
    {
        addUserDialog.setContentView(R.layout.dailog_adduser);
        addUserDialog.setCancelable(false);
        header_addUser = (TextView) addUserDialog.findViewById(R.id.header_addUser);
        user_name_text = (EditText) addUserDialog.findViewById(R.id.user_name_text);
        user_mobile = (EditText) addUserDialog.findViewById(R.id.user_mobile);
        ok_addUser = (Button) addUserDialog.findViewById(R.id.ok_addUser);
        cancel_addUser = (Button) addUserDialog.findViewById(R.id.cancel_addUser);
        header_addUser.setText("Add User To: "+name);

        ok_addUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(String.valueOf(user_name_text.getText()).isEmpty())
                {
                    user_name_text.setError("Field can't be empty");
                    return;
                }

                if(user_name_text.getText().toString().trim().length() == 0)
                {
                    user_name_text.setError("Field can't be empty");
                    return;
                }

                if(String.valueOf(user_mobile.getText()).isEmpty())
                {
                    user_mobile.setError("Field can't be empty");
                    return;
                }

                if(user_mobile.getText().toString().trim().length() == 0)
                {
                    user_mobile.setError("Field can't be empty");
                    return;
                }
                Toast.makeText(mCtx,user_name_text.getText().toString()+": "+user_mobile.getText().toString(),Toast.LENGTH_SHORT).show();
                addUserDialog.dismiss();
            }
        });

        cancel_addUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addUserDialog.dismiss();
            }
        });

        addUserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addUserDialog.show();
    }

    //Remove User From Pump
    public void removeUserFromPump(int position, String name)
    {
        removeUserDialog.setContentView(R.layout.dailog_removeuser);
        removeUserDialog.setCancelable(false);
        header_removeUser = (TextView) removeUserDialog.findViewById(R.id.header_removeUser);
        okay_Remove_user = (Button) removeUserDialog.findViewById(R.id.okay_Remove_user);
        header_removeUser.setText("Remove User For: "+name);
        okay_Remove_user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                removeUserDialog.dismiss();
            }
        });
        removeUserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        removeUserDialog.show();
    }

    //Fail Dialog Display
    public void showDialogFailed(String msg)
    {
        messageDialog.setContentView(R.layout.dailog_failed);
        messageDialog.setCancelable(false);
        message = (TextView) messageDialog.findViewById(R.id.message);
        okay_red = (Button) messageDialog.findViewById(R.id.okayred);
        message.setText(msg);
        okay_red.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                messageDialog.dismiss();
            }
        });
        messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }

    //HTTP Post Request Function
    public int requestOperation(final String name, String URL)
    {
        final int[] responseFlag = {0};
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(response.trim().equals("success"))
                        {
                            responseFlag[0] = 1;
                            Log.d("DeleteResponse",String.valueOf(responseFlag[0]));
                        }

                        else
                        {
                            responseFlag[0] = 0;
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(mCtx,String.valueOf(error),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String, String> getParams()
            {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("name", name);
                return MyData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);

        return responseFlag[0];
    }
}

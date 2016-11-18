package com.akshat_maheshwari.shareit;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Akshat Maheshwari on 17-11-2016.
 */

public class ReceiverFileListAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<ReceiverFileProgress> receiverFileProgressArrayList;

    public ReceiverFileListAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.receiverFileProgressArrayList = new ArrayList<>();
        for (String f: objects) {
            this.receiverFileProgressArrayList.add(new ReceiverFileProgress(f));
        }
        System.out.println(this.receiverFileProgressArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.receiver_file_progress_list_item, null);
        }

        TextView tvFileName = (TextView) view.findViewById(R.id.tvFileName);
        TextView tvFileReceivedPercentage = (TextView) view.findViewById(R.id.tvFileReceivedPercentage);
        TextView tvTimeTaken = (TextView) view.findViewById(R.id.tvTimeTaken);
        Button bViewFile = (Button) view.findViewById(R.id.bViewFile);

        final ReceiverFileProgress receiverFileProgress = receiverFileProgressArrayList.get(position);
        System.out.println("receiverFileProgress == null: " + receiverFileProgress == null);
        if (receiverFileProgress != null) {
            tvFileName.setText(receiverFileProgress.getFileName());
            System.out.println("ad: " + receiverFileProgress.getFileName());
            System.out.println("ad: " + receiverFileProgress.getFile() == null);
            if (receiverFileProgress.getFile() != null) {
                tvFileReceivedPercentage.setText((receiverFileProgress.getBytesSent() / receiverFileProgress.getFile().length()) * 100 + "% received");
                bViewFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri path = Uri.fromFile(receiverFileProgress.getFile());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setDataAndType(path, getMimeType(receiverFileProgress.getFile().getAbsolutePath()));
                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            System.out.println(e.toString());
                            e.printStackTrace();
                        }
                    }
                });
                bViewFile.setEnabled(true);
            }
            if (receiverFileProgress.getTimeTaken() != 0) {
                tvTimeTaken.setText(String.format("%.3f", receiverFileProgress.getTimeTaken() / Math.pow(10, 9)) + " sec");
            }
        }

        return view;
    }

    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}

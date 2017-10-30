package com.khouse.immi.chuck24hoursbible;

/**
 * Created by Immi on 4/27/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    MainActivity activity;
    Context context;
    List<RowItem> rowItems;
    MediaPlayer mp = null;
    SeekBar sb;

    CustomAdapter(Context context, List<RowItem> rowItems, SeekBar sb, MainActivity activity) {
        this.activity = activity;
        this.context = context;
        this.rowItems = rowItems;
        this.sb = sb;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    /* private view holder class */
    public class ViewHolder {
        ImageView profile_pic;
        TextView topic_name;
        TextView status;
        TextView contactType;
        ImageView speaker;
        ImageView vid;
        ImageView dlspeaker;
        ImageView dlvid;
        RelativeLayout lpbr;
        ProgressBar pbr;
        TextView tpbr;
        ImageView iv;
        ImageView ivdel;
        RelativeLayout lpbr1;
        ProgressBar pbr1;
        TextView tpbr1;
        ImageView iv1;
        ImageView ivdel1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.topic_name = (TextView) convertView
                    .findViewById(R.id.member_name);
            holder.profile_pic = (ImageView) convertView
                    .findViewById(R.id.profile_pic);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.speaker = (ImageView) convertView.findViewById(R.id.speaker);
            holder.vid = (ImageView) convertView.findViewById(R.id.vid);
            //holder.dlspeaker = (ImageView) convertView.findViewById(R.id.download_aud);
            //holder.dlvid = (ImageView) convertView.findViewById(R.id.download_vid);
            holder.lpbr = (RelativeLayout) convertView.findViewById(R.id.audLayout);
            holder.lpbr.setOnClickListener(new imageViewClickListener(position, "DlAudio"));
            holder.pbr = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.tpbr = (TextView) convertView.findViewById(R.id.txtProgress);
            holder.iv = (ImageView)convertView.findViewById(R.id.ivPrg);
            holder.ivdel = (ImageView)convertView.findViewById(R.id.ivdel);

            holder.lpbr1 = (RelativeLayout) convertView.findViewById(R.id.vidLayout);
            holder.lpbr1.setOnClickListener(new imageViewClickListener(position, "DlVideo"));
            holder.pbr1 = (ProgressBar) convertView.findViewById(R.id.progressBar1);
            holder.tpbr1 = (TextView) convertView.findViewById(R.id.txtProgress1);
            holder.iv1 = (ImageView)convertView.findViewById(R.id.ivPrg1);
            holder.ivdel1 = (ImageView)convertView.findViewById(R.id.ivdel1);

            RowItem row_pos = rowItems.get(position);
            holder.speaker.setOnClickListener(new imageViewClickListener(position, "Speaker"));
            holder.vid.setOnClickListener(new imageViewClickListener(position, "Video"));
            //holder.dlspeaker.setOnClickListener(new imageViewClickListener(position, "DlSpeaker"));
            //holder.dlvid.setOnClickListener(new imageViewClickListener(position, "DlVideo"));

            holder.profile_pic.setImageResource(row_pos.getTopic_pic_id());
            holder.topic_name.setText(row_pos.getTopic_name());
            holder.status.setText(row_pos.getStatus());
            //holder.contactType.setText(row_pos.getContactType());

            if (row_pos.getAudDownloaded()){
                holder.tpbr.setText("Del");
                holder.ivdel.setVisibility(View.VISIBLE);
                holder.iv.setVisibility(View.GONE);
            } else{
                holder.tpbr.setText("Dld");
            }

            if (row_pos.getVidDownloaded()){
                holder.tpbr1.setText("Del");
                holder.ivdel1.setVisibility(View.VISIBLE);
                holder.iv1.setVisibility(View.GONE);
            } else{
                holder.tpbr1.setText("Dld");
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateVidPrgress(final int dsize, final int tot, final View v1){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //pb.setProgress(downloadedSize);
                float per = ((float)dsize/tot) * 100;
                ProgressBar pb = (ProgressBar)v1.findViewById(R.id.progressBar1);
                pb.setProgress(Math.round(per));
                TextView txtProgress = (TextView)v1.findViewById(R.id.txtProgress1);
                txtProgress.setText(Math.round(per) + "%");
            }
        });
    }

    public void updateVidComplete(final View v1){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                TextView txtProgress = (TextView)v1.findViewById(R.id.txtProgress1);
                txtProgress.setText("Del");
                txtProgress.setVisibility(View.GONE);
                ImageView iv2 = (ImageView)v1.findViewById(R.id.ivdel1);
                iv2.setVisibility(View.VISIBLE);
            }
        });
    }

    public void updateAudPrgress(final int dsize, final int tot, final View v1){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //pb.setProgress(downloadedSize);
                float per = ((float)dsize/tot) * 100;
                ProgressBar pb = (ProgressBar)v1.findViewById(R.id.progressBar);
                pb.setProgress(Math.round(per));
                TextView txtProgress = (TextView)v1.findViewById(R.id.txtProgress);
                txtProgress.setText(Math.round(per) + "%");
            }
        });
    }

    public void updateAudComplete(final View v1){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                TextView txtProgress = (TextView)v1.findViewById(R.id.txtProgress);
                txtProgress.setText("Del");
                txtProgress.setVisibility(View.GONE);
                ImageView iv2 = (ImageView)v1.findViewById(R.id.ivdel);
                iv2.setVisibility(View.VISIBLE);
            }
        });
    }

    class imageViewClickListener implements View.OnClickListener {
        int position;
        String act;

        public imageViewClickListener(int pos, String act) {
            this.position = pos;
            this.act = act;
        }

        public void onClick(final View v) {
            if (act == "DlAudio"){
                String stt = ((TextView)v.findViewById(R.id.txtProgress)).getText().toString();
                if (stt != "Del" && stt != "Dld"){
                    return;
                }
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            RowItem ri = rowItems.get(position);
                            if (ri.getAudDownloaded()){
                                File f1 = new File(ri.getAudUrlPath());
                                f1.delete();
                                return;
                            }
                            String path = "http://immanuel.co/chuck/audio/" + position + ".mp3";
                            URL url = new URL(path);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                            urlConnection.setRequestMethod("GET");
                            urlConnection.setDoOutput(true);

                            //connect
                            urlConnection.connect();

                            //create a new file, to save the downloaded file
                            String topath = context.getCacheDir().toString();
                            File file = new File(topath, position + ".mp3");

                            FileOutputStream fileOutput = new FileOutputStream(file);

                            //Stream used for reading the data from the internet
                            InputStream inputStream = urlConnection.getInputStream();

                            //this is the total size of the file which we are downloading
                            final int totalSize = urlConnection.getContentLength();

                            final ImageView iv = (ImageView)v.findViewById(R.id.ivPrg);
                            final TextView tp = (TextView)v.findViewById(R.id.txtProgress);
                            //Start progress
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    iv.setVisibility(View.GONE);
                                    tp.setVisibility(View.VISIBLE);
                                }
                            });

                            //create a buffer...
                            byte[] buffer = new byte[1024];
                            int bufferLength = 0;
                            int downloadedSize = 0;
                            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                                fileOutput.write(buffer, 0, bufferLength);
                                downloadedSize += bufferLength;
                                final  int dsize = downloadedSize;
                                // update the progressbar //
                                updateAudPrgress(downloadedSize, totalSize, v);
                            }
                            //close the output stream when complete //
                            fileOutput.close();
                            ri.setAudDownloaded(true);
                            ri.setAudUrlPath(file.getAbsolutePath());
                            updateAudComplete(v);
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    CommonPreference.SaveFile(position + ".mp3", context);
                                }
                            });

                        } catch (final MalformedURLException e) {
                            activity.showError("Error : MalformedURLException " + e);
                            e.printStackTrace();
                        } catch (final IOException e) {
                            activity.showError("Error : IOException " + e);
                            e.printStackTrace();
                        }
                        catch (final Exception e) {
                            activity.showError("Error : Please check your internet connection " + e);
                        }
                    }
                }).start();
            }
            if (act == "DlVideo"){
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            RowItem ri = rowItems.get(position);
                            String stt = ((TextView)v.findViewById(R.id.txtProgress1)).getText().toString();
                            if (stt != "Del" && stt != "Dld"){
                                return;
                            }
                            if (ri.getVidDownloaded()){
                                File f1 = new File(ri.getVidUrlPath());
                                f1.delete();
                                return;
                            }
                            String path = "http://immanuel.co/chuck/video/" + position + ".avi";
                            URL url = new URL(path);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                            urlConnection.setRequestMethod("GET");
                            urlConnection.setDoOutput(true);

                            //connect
                            urlConnection.connect();

                            //create a new file, to save the downloaded file
                            String topath = context.getCacheDir().toString();
                            File file = new File(topath, position + ".avi");

                            FileOutputStream fileOutput = new FileOutputStream(file);

                            //Stream used for reading the data from the internet
                            InputStream inputStream = urlConnection.getInputStream();

                            //this is the total size of the file which we are downloading
                            final int totalSize = urlConnection.getContentLength();

                            final ImageView iv1 = (ImageView)v.findViewById(R.id.ivPrg1);
                            final TextView tp1 = (TextView)v.findViewById(R.id.txtProgress1);
                            //Start progress
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    iv1.setVisibility(View.GONE);
                                    tp1.setVisibility(View.VISIBLE);
                                }
                            });

                            //create a buffer...
                            byte[] buffer = new byte[1024];
                            int bufferLength = 0;
                            int downloadedSize = 0;
                            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                                fileOutput.write(buffer, 0, bufferLength);
                                downloadedSize += bufferLength;
                                final  int dsize = downloadedSize;
                                // update the progressbar //
                                updateVidPrgress(downloadedSize, totalSize, v);
                            }
                            //close the output stream when complete //
                            fileOutput.close();
                            ri.setVidDownloaded(true);
                            ri.setVidUrlPath(file.getAbsolutePath());
                            updateVidComplete(v);
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    CommonPreference.SaveFile(position + ".avi", context);
                                }
                            });

                        } catch (final MalformedURLException e) {
                            activity.showError("Error : MalformedURLException " + e);
                            e.printStackTrace();
                        } catch (final IOException e) {
                            activity.showError("Error : IOException " + e);
                            e.printStackTrace();
                        }
                        catch (final Exception e) {
                            activity.showError("Error : Please check your internet connection " + e);
                        }
                    }
                }).start();
            }
            if (act == "Video"){
                try {
                    if (mp != null) {
                        mp.stop();
                        mp = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent myIntent = new Intent(context,
                        VideoViewActivity.class);
                myIntent.putExtra("Posn", Integer.toString(position));
                String upa = rowItems.get(position).getVidUrlPath();
                myIntent.putExtra("path", upa);
                context.startActivity(myIntent);
            }
            if (act == "Speaker") {
                ImageView imageView = (ImageView) v;
                Toast.makeText(context, "Streaming...." + position,
                        Toast.LENGTH_SHORT).show();
                if (imageView.getTag() == null || imageView.getTag() == "Play") {
                    imageView.setTag("Pause");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.aud_stop, context.getApplicationContext().getTheme()));
                    } else {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.aud_stop));
                    }
                    //String path = "http://immanuel.co/chuck/audio/" + position + ".mp3";
                    String path = rowItems.get(position).getAudUrlPath();
                    try {
                        if (mp != null) {
                            mp.stop();
                            mp = null;
                        }
                        activity.updateAudText(position);
                        mp = new MediaPlayer();
                        mp.setDataSource(context, Uri.parse(path));//Write your location here
                        mp.prepare();
                        getSeekBarStatus();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            // Close the progress bar and play the video
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    imageView.setTag("Play");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.aud_play, context.getApplicationContext().getTheme()));
                    } else {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.aud_play));
                    }
                    if (mp != null) {
                        mp.stop();
                        mp = null;
                    }
                }
            }
        }
    }

    public void getSeekBarStatus(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                // mp is your MediaPlayer
                // progress is your ProgressBar

                int currentPosition = 0;
                int total = mp.getDuration();
                sb.setMax(total);
                while (mp != null && currentPosition < total) {
                    try {
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }

                }
            }
        }).start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress=0;

            @Override
            public void onProgressChanged(final SeekBar seekBar, int ProgressValue, boolean fromUser) {
                if (fromUser && mp != null) {
                    mp.seekTo(ProgressValue);//if user drags the seekbar, it gets the position and updates in textView.
                }
                final long mMinutes=(ProgressValue/1000)/60;//converting into minutes
                final int mSeconds=((ProgressValue/1000)%60);//converting into seconds
                if (progress <= 50) {
                    setProgressBarColor(sb, Color.rgb(
                            255 - (255 / 100 * (100 - progress * 2)),
                            255, 0));

                } else {
                    setProgressBarColor(sb, Color.rgb(255,
                            255 - (255 / 100 * (progress - 50) * 2), 0));

                }
                //SongProgress.setText(mMinutes+":"+mSeconds);
            }

            public void setProgressBarColor(SeekBar seakBar, int newColor) {
                LayerDrawable ld = (LayerDrawable) seakBar.getProgressDrawable();
                ClipDrawable d1 = (ClipDrawable) ld
                        .findDrawableByLayerId(R.id.progressshape);
                d1.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
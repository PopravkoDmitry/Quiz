package com.example.qaa;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Level1 extends AppCompatActivity {

    Dialog dialog;
    Dialog dialogEnd;

    public int numLeft;
    public int numRight;
    Array array = new Array();
    Random random = new Random();
    public int count = 0;
    private final String userKey = "User";
    private final DatabaseReference pomsQuiz = FirebaseDatabase.getInstance().getReference(userKey);
    private Date startTime;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);

        TextView text_levels = findViewById(R.id.text_levels);
        text_levels.setText(R.string.level1);

        final ImageView img_left = (ImageView) findViewById(R.id.img_left);
        img_left.setClipToOutline(true);

        final ImageView img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setClipToOutline(true);

        final TextView text_left = findViewById(R.id.text_left); //Путь к левому тексту
        final TextView text_right = findViewById(R.id.text_right); //Путь к правому тексту

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.previewdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);


        Button btncontinue = (Button) dialog.findViewById(R.id.btncontinue);
        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startTime = dateFormat.parse(dateFormat.format(new Date()));
                } catch (ParseException ex) {
                    System.out.println(ex.getMessage());
                }

                dialog.dismiss();
            }
        });

        dialog.show();


        dialogEnd = new Dialog(this);
        dialogEnd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEnd.setContentView(R.layout.dialogend);
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogEnd.setCancelable(false);


        final EditText editText = dialogEnd.findViewById(R.id.name);
        Button btncontinue2 = (Button) dialogEnd.findViewById(R.id.btncontinue);
        btncontinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date endTime = new Date();

                try {
                    endTime = dateFormat.parse(dateFormat.format(new Date()));
                } catch (ParseException ex) {
                    System.out.println(ex.getMessage());
                }

                long difference = endTime.getTime() - startTime.getTime();

                pomsQuiz.push().setValue(editText.getText().toString() + " " + (difference / 1000) + " сек");
                Intent intent = new Intent(Level1.this, MainActivity.class);
                startActivity(intent);
                finish();

                dialogEnd.dismiss();

            }
        });



        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(Level1.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {

                }
            }
        });

        //массив для прогресса
        final int[] progress = {
                R.id.point1,
                R.id.point2,
                R.id.point3,
                R.id.point4,
                R.id.point5,
                R.id.point6,
                R.id.point7,
                R.id.point8,
                R.id.point9,
                R.id.point10,
        };

        final Animation a = AnimationUtils.loadAnimation(Level1.this, R.anim.alpha);

        numLeft = random.nextInt(7);
       // img_left.setImageResource(array.images1[numLeft]);
        Glide.with(this).load(array.images1[1]).into(img_left);
        text_left.setText(array.texts1[1]);

        numRight = random.nextInt(7);

        while (numLeft == numRight) {
            numRight = random.nextInt(7);
        }

        //img_right.setImageResource(R.drawable.onelevel_one);
        Glide.with(this).load(array.images1[1]).into(img_right);

        text_right.setText(array.texts1[1]);

        //нажатие на левую картинку
        img_left.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //касание картинки
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //коснулся кортинки
                    img_right.setEnabled(false);
                    if (numLeft > numRight) {
                        img_left.setImageResource(R.drawable.img_true);
                    } else {
                        img_left.setImageResource(R.drawable.img_false);
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //отпустил касание
                    if (numLeft > numRight) {
                        if (count < 10) {
                            count++;
                        }

                        for (int i = 0; i < 10; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }

                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }

                    } else {

                        if (count > 0) {
                            if (count == 1) {
                                count = 0;
                            } else {
                                count = count - 2;
                            }
                        }
                        for (int i = 0; i < 9; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }

                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }

                    }
                    if (count == 10) {
                        //выход из уровня
                        SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                        final int level = save.getInt("Level", 1);

                        if (level > 1) {

                        } else {
                            SharedPreferences.Editor editor = save.edit();
                            editor.putInt("Level", 2);
                            editor.commit();
                        }

                        dialogEnd.show();

                    } else {
                        numLeft = random.nextInt(7);
                        //img_left.setImageResource(array.images1[numLeft]);
                        Glide.with(getApplicationContext()).load(array.images1[1]).into(img_left);

                        img_left.startAnimation(a);
                        text_left.setText(array.texts1[1]);

                        numRight = random.nextInt(7);

                        while (numLeft == numRight) {
                            numRight = random.nextInt(7);
                        }

                        //img_right.setImageResource(array.images1[numRight]);
                        Glide.with(getApplicationContext()).load(array.images1[1]).into(img_right);

                        img_right.startAnimation(a);
                        text_right.setText(array.texts1[1]);
                        img_right.setEnabled(true);
                    }
                }

                return true;
            }
        });

        img_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //касание картинки
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //коснулся кортинки
                    img_left.setEnabled(false);
                    if (numRight > numLeft) {
                        img_right.setImageResource(R.drawable.img_true);
                    } else {
                        img_right.setImageResource(R.drawable.img_false);
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //отпустил касание
                    if (numRight > numLeft) {
                        if (count < 10) {
                            count++;
                        }

                        for (int i = 0; i < 10; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }

                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }

                    } else {

                        if (count > 0) {
                            if (count == 1) {
                                count = 0;
                            } else {
                                count = count - 2;
                            }
                        }
                        for (int i = 0; i < 9; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }

                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }

                    }
                    if (count == 10) {
                        //выход из уровня

                        SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                        final int level = save.getInt("Level", 1);

                        if (level > 1) {

                        } else {
                            SharedPreferences.Editor editor = save.edit();
                            editor.putInt("Level", 2);
                            editor.commit();
                        }

                        dialogEnd.show();

                    } else {
                        numRight = random.nextInt(7);
                        //img_right.setImageResource(array.images1[numRight]);
                        Glide.with(getApplicationContext()).load(array.images1[1]).into(img_right);

                        img_right.startAnimation(a);
                        text_right.setText(array.texts1[1]);

                        numLeft = random.nextInt(7);

                        while (numRight == numLeft) {
                            numLeft = random.nextInt(7);
                        }

                        //img_left.setImageResource(array.images1[numLeft]);
                        Glide.with(getApplicationContext()).load(array.images1[1]).into(img_left);

                        img_left.startAnimation(a);
                        text_left.setText(array.texts1[numLeft]);
                        img_left.setEnabled(true);
                    }
                }

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        try {

            Intent intent = new Intent(Level1.this, MainActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {

        }

    }
}

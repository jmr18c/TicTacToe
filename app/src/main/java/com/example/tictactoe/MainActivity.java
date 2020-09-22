package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private Button buttonReset;

    private boolean p1Turn = true;

    private int rdCount;

    private int p1Points;
    private int p2Points;

    private TextView textViewP1;
    private TextView textViewP2;

    private MediaPlayer winMP;
    private MediaPlayer drawMP;

    ImageView winImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        winImageView = findViewById(R.id.trophy_IV);

        textViewP1 = findViewById(R.id.text_view_p1);
        textViewP2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++)
        {
            for (int j= 0; j < 3; j++)
            {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {


        if (!((Button) v).getText().toString().equals(""))
        {
            return;
        }

        if (p1Turn)
        {
            ((Button) v).setText("X");
            v.setBackgroundResource(R.drawable.marvel_2);
        } else {
            ((Button) v).setText("O");
            v.setBackgroundResource(R.drawable.dc);
        }

        rdCount++;

        if (checkForWin())
        {
            if (p1Turn) {
                p1Wins();
            } else {
                p2Wins();
            }
        } else if (rdCount == 9){
            draw();
        } else {
            p1Turn = !p1Turn;
        }
    }

    private boolean checkForWin()
    {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++)
        {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(""))
            {
                return true;
            }
        }

        for (int i = 0; i < 3; i++)
        {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(""))
            {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
        {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(""))
        {
            return true;
        }

        return false;
    }

    private void p1Wins()
    {
        winMP = MediaPlayer.create(this, R.raw.win);
        winMP.start();

        winImageView.setVisibility(View.VISIBLE);
        
        p1Points++;
        Toast.makeText(this, "Marvel Wins!", Toast.LENGTH_SHORT). show();
        updatePointsText();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                winImageView.setVisibility(View.INVISIBLE);
            }
        }, 3000);

    }

    private void p2Wins()
    {
        winMP = MediaPlayer.create(this, R.raw.win);
        winMP.start();

        winImageView.setVisibility(View.VISIBLE);

        p2Points++;
        Toast.makeText(this, "DC Wins!", Toast.LENGTH_SHORT). show();
        updatePointsText();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                winImageView.setVisibility(View.INVISIBLE);
            }
        }, 3000);
    }

    private void draw()
    {
        drawMP = MediaPlayer.create(this, R.raw.draw_crickets);
        drawMP.start();

        Toast.makeText(this, "Draw (Marvel is still better though)!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 3000);
    }

    private void updatePointsText()
    {
        textViewP1.setText("Marvel: " + p1Points);
        textViewP2.setText("DC:     " + p2Points);
    }

    private void resetBoard()
    {
        Button[][] resetButton = new Button[3][3];

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(buttonReset.getBackground());
            }
        }

        rdCount = 0;
        p1Turn = true;
    }

    private void resetGame()
    {
        p1Points = 0;
        p2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", rdCount);
        outState.putInt("player1Points", p1Points);
        outState.putInt("player2Points", p2Points);
        outState.putBoolean("player1Turn", p1Turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        rdCount = savedInstanceState.getInt("roundCount");
        p1Points = savedInstanceState.getInt("player1Points");
        p2Points = savedInstanceState.getInt("player2Points");
        p1Turn = savedInstanceState.getBoolean("player1Turn");

        restoreImages();
    }

    void restoreImages() {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                Button thisButton = buttons[i][j];

                if (thisButton.getText().equals("X")) {
                    thisButton.setBackgroundResource(R.drawable.marvel_2);
                } else if (thisButton.getText().equals("O")) {
                    thisButton.setBackgroundResource(R.drawable.dc);
                }
            }
        }
    }
}
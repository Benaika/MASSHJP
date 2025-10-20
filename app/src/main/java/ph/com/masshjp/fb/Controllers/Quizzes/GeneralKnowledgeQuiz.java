package ph.com.masshjp.fb.Controllers.Quizzes;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.Controllers.Categories.GeneralKnowledge;
import ph.com.masshjp.fb.Controllers.Dashboard.DashboardActivity;
import ph.com.masshjp.fb.R;

public class GeneralKnowledgeQuiz extends BaseActivity implements View.OnClickListener {

    TextView noOfQuestions, tvQuestions;
    Button btnOptionA,btnOptionB,btnOptionC,btnOptionD,btnSubmit;

    int score = 0;
    int totalQuestions = GeneralKnowledge.questions.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";
    int choicesColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_knowledge_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setToolbar(this, R.id.toolbar, "General Knowledge");

        // Apply transition animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);

        choicesColor = getResources().getColor(R.color.lightMaroon);

        // Initializing UI elements for a quiz app:
        noOfQuestions = findViewById(R.id.txt_numberOfQuestions);
        tvQuestions = findViewById(R.id.tv_questions);
        btnOptionA = findViewById(R.id.txt_optionA);
        btnOptionB = findViewById(R.id.txt_optionB);
        btnOptionC = findViewById(R.id.txt_optionC);
        btnOptionD = findViewById(R.id.txt_optionD);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnOptionA.setOnClickListener(this);
        btnOptionB.setOnClickListener(this);
        btnOptionC.setOnClickListener(this);
        btnOptionD.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        noOfQuestions.setText(String.valueOf(totalQuestions));

        loadNewQuestion();
    }
    @Override
    public void onClick(View v) {
        btnOptionA.setBackgroundColor(choicesColor);
        btnOptionB.setBackgroundColor(choicesColor);
        btnOptionC.setBackgroundColor(choicesColor);
        btnOptionD.setBackgroundColor(choicesColor);

        Button clickedButton = (Button) v;
        if (clickedButton.getId() == R.id.btnSubmit) {
            if (selectedAnswer.equals("")) {
                // User hasn't chosen an answer, show a toast
                Toast.makeText(this, "Please choose an answer", Toast.LENGTH_SHORT).show();
                return; // Don't proceed to the next question
            }
            if (selectedAnswer.equals(GeneralKnowledge.answers[currentQuestionIndex])) {
                score++;
            }
            currentQuestionIndex++;
            selectedAnswer = ""; // Reset selected answer
            loadNewQuestion();
        } else {
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.RED);
        }
    }
    void loadNewQuestion(){

        if(currentQuestionIndex == totalQuestions){
            finishQuiz();
            return;
        }

        // Update the current question number in the TextView
        noOfQuestions.setText((currentQuestionIndex + 1) + " / " + totalQuestions);

        tvQuestions.setText(GeneralKnowledge.questions[currentQuestionIndex]);
        btnOptionA.setText(GeneralKnowledge.choices[currentQuestionIndex][0]);
        btnOptionB.setText(GeneralKnowledge.choices[currentQuestionIndex][1]);
        btnOptionC.setText(GeneralKnowledge.choices[currentQuestionIndex][2]);
        btnOptionD.setText(GeneralKnowledge.choices[currentQuestionIndex][3]);
    }
    void finishQuiz() {

        String passStatus = (score > totalQuestions * 0.60) ? "Passed" : "Failed";

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is " + score + " out of " + totalQuestions)
                .setPositiveButton("Restart", (dialog, which) -> restartQuiz())
                .setNegativeButton("Homepage", (dialog, which) -> homepage())
                .setCancelable(false)
                .show();
    }
    void restartQuiz(){
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();
    }
    void homepage(){
        //Intents the activity to Homepage
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.JsonReader
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mobiory.R



class QuizScreen : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private lateinit var currentQuestion: Question
    private var currentQuestionIndex = 0
    private var score = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)  // Assuming your layout file is named quiz.xml

        // Lire le fichier JSON (Read the JSON file)
        val inputStream = resources.assets.open("questions.json")
        val jsonReader = JsonReader(inputStream.reader())
        questions = parseQuestions(jsonReader)

        // Initialiser la première question (Initialize the first question)
        currentQuestion = questions[currentQuestionIndex]
        displayQuestion()

        // Bouton "Suivant" (Next Button)
        val nextButton = findViewById<Button>(R.id.button_next)
        nextButton.setOnClickListener {
            // Vérifier la réponse (Check the answer)
            val selectedAnswer = findViewById<RadioGroup>(R.id.radio_group_answers).checkedRadioButtonId
            if (selectedAnswer != -1) {
                val selectedAnswerIndex = findViewById<RadioButton>(selectedAnswer).tag as Int
                if (selectedAnswerIndex == currentQuestion.correctAnswerIndex) {
                    score++
                }
            }

            // Passer à la question suivante (Move to the next question)
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                currentQuestion = questions[currentQuestionIndex]
                displayQuestion()
            } else {
                // Afficher le score final (Show the final score)
                val scoreTextView = findViewById<TextView>(R.id.text_view_question)
                scoreTextView.text = "Votre score est : $score/${questions.size}"
            }
        }
    }

    private fun displayQuestion() {
        // Afficher le texte de la question (Show the question text)
        val questionTextView = findViewById<TextView>(R.id.text_view_question)
        questionTextView.text = currentQuestion.question

        // Afficher les options de réponse (Show the answer options)
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group_answers)
        radioGroup.removeAllViews()
        for (i in 0 until currentQuestion.options.size) {
            val optionRadioButton = RadioButton(this)
            optionRadioButton.text = currentQuestion.options[i]
            optionRadioButton.tag = i
            radioGroup.addView(optionRadioButton)
        }
    }

    private fun parseQuestions(jsonReader: JsonReader): List<Question> {
        val questions = mutableListOf<Question>()
        jsonReader.beginArray()
        while (jsonReader.hasNext()) {
            jsonReader.beginObject()
            val options = mutableListOf<String>()
            jsonReader.beginArray()
            while (jsonReader.hasNext()) {
                options.add(jsonReader.nextString())
            }
            jsonReader.endArray()
            val question = Question(
                jsonReader.nextString(),               // Read question string
                options,                               // Use the parsed options list
                jsonReader.nextInt()                  // Read correct answer index
            )

            questions.add(question)
            jsonReader.endObject()
        }
        jsonReader.endArray()
        return questions
    }
}


data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

package com.example.hraukol13.data

object DemoData {

    val questions = listOf(
        Question.QuestionEntity(
            questionText = "Který prvek má chemickou značku Au?",
            correctAnswer = "Zlato",
            wrongAnswer1 = "Stříbro",
            wrongAnswer2 = "Hliník"
        ),
        Question.QuestionEntity(
            questionText = "Který oceán je největší na světě?",
            correctAnswer = "Tichý oceán",
            wrongAnswer1 = "Atlantský oceán",
            wrongAnswer2 = "Indický oceán"
        ),
        Question.QuestionEntity(
            questionText = "V jakém roce začala první světová válka?",
            correctAnswer = "1914",
            wrongAnswer1 = "1918",
            wrongAnswer2 = "1939"
        ),
        Question.QuestionEntity(
            questionText = "Kdo napsal tragédii Romeo a Julie?",
            correctAnswer = "William Shakespeare",
            wrongAnswer1 = "Charles Dickens",
            wrongAnswer2 = "Oscar Wilde"
        ),
        Question.QuestionEntity(
            questionText = "Jaké je hlavní město Japonska?",
            correctAnswer = "Tokio",
            wrongAnswer1 = "Kjóto",
            wrongAnswer2 = "Ósaka"
        ),
        Question.QuestionEntity(
            questionText = "Kolik kontinentů je na planetě Zemi?",
            correctAnswer = "7",
            wrongAnswer1 = "6",
            wrongAnswer2 = "5"
        ),
        Question.QuestionEntity(
            questionText = "Které zvíře je nejrychlejším suchozemským savcem?",
            correctAnswer = "Gepard",
            wrongAnswer1 = "Lev",
            wrongAnswer2 = "Antilopa"
        ),
        Question.QuestionEntity(
            questionText = "Kdo byl prvním člověkem na Měsíci?",
            correctAnswer = "Neil Armstrong",
            wrongAnswer1 = "Buzz Aldrin",
            wrongAnswer2 = "Yuri Gagarin"
        ),
        Question.QuestionEntity(
            questionText = "Jak se jmenuje nejdelší řeka světa?",
            correctAnswer = "Nil",
            wrongAnswer1 = "Amazonka",
            wrongAnswer2 = "Mississippi"
        ),
        Question.QuestionEntity(
            questionText = "Kolik strun má standardní klasická kytara?",
            correctAnswer = "6",
            wrongAnswer1 = "4",
            wrongAnswer2 = "5"
        )
    )

    val sampleResults = listOf(
        // Oprava: Použití celého balíčku zabrání kolizi s kotlin.Result
        com.example.hraukol13.data.Result.ResultEntity(
            playerName = "DemoHrač",
            score = 3
        )
    )
}

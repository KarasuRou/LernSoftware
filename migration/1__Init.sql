CREATE TABLE IF NOT EXISTS Subject (
    ID INTEGER NOT NULL CONSTRAINT Subject_pk PRIMARY KEY autoincrement,
    Name TEXT NOT NULL,
    PicturePath TEXT
);
CREATE UNIQUE INDEX IF NOT EXISTS Subject_Name_uindex ON Subject (Name);

CREATE TABLE IF NOT EXISTS Folder (
    ID INTEGER NOT NULL CONSTRAINT Folder_pk PRIMARY KEY AUTOINCREMENT,
    f_ID INTEGER NOT NULL CONSTRAINT Folder__Subject_fk REFERENCES Subject ON UPDATE CASCADE ON DELETE CASCADE,
    Name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Question (
    ID INTEGER NOT NULL CONSTRAINT Question_pk PRIMARY KEY AUTOINCREMENT,
    f_ID INTEGER NOT NULL CONSTRAINT Question__Folder_fk REFERENCES Folder ON UPDATE CASCADE ON DELETE CASCADE,
    QuestionType TEXT NOT NULL
        check(QuestionType = "UNSET" OR
              QuestionType = "DirectQuestion" OR
              QuestionType = "WordsQuestion" OR
              QuestionType = "MultipleChoiceQuestion"
            )
);

CREATE TABLE IF NOT EXISTS Question_Params (
    ID INTEGER NOT NULL CONSTRAINT Question_Params_pk PRIMARY KEY AUTOINCREMENT,
    f_ID INTEGER NOT NULL CONSTRAINT Question_Params__Question_fk REFERENCES Question ON UPDATE CASCADE ON DELETE CASCADE,
    type Text NOT NULL
        check(type = "questionMessage" OR
              type = "answer" OR
              type = "extraParameter"
            ),
    value NOT NULL
);

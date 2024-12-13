package com.literify.util

import com.literify.data.remote.model.BookGenreResponse
import com.literify.data.remote.model.BookResponse

object DummyData {
    val bookResponseDummy = BookResponse(
        id = "1",
        title = "Jean Arthur: The Actress Nobody Knew",
        description = "The luminous star of Mr. Smith Goes to Washington, Shane, and other classic films was, as the subtitle aptly puts it, \"the actress nobody knew.\" Jean Arthur (1900-91) kept her personal life private, disdained the Hollywood publicity machine, and was called \"difficult\" because of her perfectionism and remoteness from costars on the movie set. John Oller, a lawyer, tracked down kinsfolk and friends never before interviewed to capture the elusive personality of a free spirit best embodied in her favorite role, Peter Pan. Arthur herself might have appreciated his warm, respectful portrait.",
        authors = "John Oller",
        genre = "Biography & Autobiography",
        publisher = "Limelight",
        price = 12.46,
        image = "https://books.google.com/books/content?id=bh9BObisnkMC&printsec=frontcover&img=1&zoom=1&source=gbs_api",
        previewLink = "http://books.google.com/books?id=bh9BObisnkMC&q=Jean+Arthur:+The+Actress+Nobody+Knew&dq=Jean+Arthur:+The+Actress+Nobody+Knew&hl=&cd=1&source=gbs_api",
        infoLink = "http://books.google.com/books?id=bh9BObisnkMC&dq=Jean+Arthur:+The+Actress+Nobody+Knew&hl=&source=gbs_api",
        averageRating = 4.42,
        ratingCount = 12,
        weightedAverage = 0.811807951,
        bagOfWords = "the luminous star of mr smith goes to washington shane and other classic films was as the subtitle aptly puts it the actress nobody knew jean arthur kept her personal life private disdained the hollywood publicity machine and was called difficult because of her perfectionism and remoteness from costars on the movie set john oller a lawyer tracked down kinsfolk and friends never before interviewed to capture the elusive personality of a free spirit best embodied in her favorite role peter pan arthur herself might have appreciated his warm respectful portrait an insightful painstakingly researched analysis of arthurs life and career raises the curtain on the complex conflicted person behind the screen personacaptures the special shine of a unique star who turned out to be a genuine eccentric chicago tribune john oller limelight biography autobiography"
    )

    val genreResponseDummy = listOf(
        BookGenreResponse("Biography & Autobiography", "https://storage.googleapis.com/genre_literify/genre/Biography%20%26%20Autobiography.png"),
        BookGenreResponse("Business & Economics", "https://storage.googleapis.com/genre_literify/genre/Business%20%26%20Economics.png"),
        BookGenreResponse("Computers", "https://storage.googleapis.com/genre_literify/genre/Computers.png"),
        BookGenreResponse("Cooking", "https://storage.googleapis.com/genre_literify/genre/Cooking.png"),
        BookGenreResponse("Fiction", "https://storage.googleapis.com/genre_literify/genre/Fiction.png"),
        BookGenreResponse("History", "https://storage.googleapis.com/genre_literify/genre/History.png"),
        BookGenreResponse("Juvenile Fiction", "https://storage.googleapis.com/genre_literify/genre/Juvenile%20Fiction.png"),
        BookGenreResponse("Religion", "https://storage.googleapis.com/genre_literify/genre/Religion.png"),
        BookGenreResponse("Self-Help", "https://storage.googleapis.com/genre_literify/genre/Self-Help.png"),
        BookGenreResponse("Social Science", "https://storage.googleapis.com/genre_literify/genre/Social%20Science.png")
    )

    val rv1Dummy = listOf(
        BookResponse(
            id = "nosIAQAAMAAJ",
            title = "It's Not All Song and Dance: A Life Behind the Scenes in the Performing Arts",
            authors = "Maxim Gershunoff, Leon Van Dyke",
            image = "https://books.google.com/books/content?id=nosIAQAAMAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        ),
        BookResponse(
            id = "bh9BObisnkMC",
            title = "Jean Arthur: The Actress Nobody Knew",
            authors = "John Oller",
            image = "https://books.google.com/books/content?id=bh9BObisnkMC&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        ),
        BookResponse(
            id = "K8cob8AuCvEC",
            title = "comeback - a mother and daughter's journey through hell and back",
            authors = "Claire Fontaine, Mia Fontaine",
            image = "https://books.google.com/books/content?id=K8cob8AuCvEC&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        ),
        BookResponse(
            id = "b2bzAAAAMAAJ",
            title = "I Promised I Would Tell: Her Poetry and Testimony During the Holocaust",
            authors = "Sonia Schreiber Weitz",
            image = "https://books.google.com/books/content?id=b2bzAAAAMAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        ),
        BookResponse(
            id = "K7xsTkA-JIsC",
            title = "Forever Liesl: A Memoir of The Sound of Music",
            authors = "Charmian Carr",
            image = "https://books.google.com/books/content?id=K7xsTkA-JIsC&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        ),
        BookResponse(
            id = "UP4yEAAAQBAJ",
            title = "Narrative of the Life of Frederick Douglass",
            authors = "FREDERICK DOUGLASS",
            image = "https://books.google.com/books/content?id=UP4yEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
        ),
        BookResponse(
            id = "HjGvQgAACAAJ",
            title = "An Autobiographical Novel (Revived Modern Classic)",
            authors = "Kenneth Rexroth, Linda Hamalian",
            image = "https://books.google.com/books/content?id=HjGvQgAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        ),
        BookResponse(
            id = "Z1_6DwAAQBAJ",
            title = "Left to Tell: Discovering God Amidst The Rwandan Holocaust",
            authors = "Immaculee Ilibagiza",
            image = "https://books.google.com/books/content?id=Z1_6DwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
        ),
        BookResponse(
            id = "vPIc7n3j7pYC",
            title = "STOLEN LIVES",
            authors = "Malika Oufkir",
            image = "https://books.google.com/books/content?id=vPIc7n3j7pYC&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        ),
        BookResponse(
            id = "UP4yEAAAQBAJ",
            title = "The Diary of Anne Frank",
            authors = "Anne Frank",
            image = "https://books.google.com/books/content?id=UP4yEAAAQBAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        )
    )
}
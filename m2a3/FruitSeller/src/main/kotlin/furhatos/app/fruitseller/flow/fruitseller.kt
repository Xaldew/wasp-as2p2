package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.order
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.CereprocVoice
import furhatos.flow.kotlin.voice.cereproc.William
import furhatos.nlu.common.*
import furhatos.util.Language
import java.util.Random


val Start = state(Interaction) {
    onEntry {
        val myVoice = CereprocVoice(name = "william", language = Language.ENGLISH_GB)
        furhat.setVoice(myVoice)
        random(
            {   furhat.say("Hi there") },
            {   furhat.say("Oh, hello there") },
            {   furhat.say("Welcome") }
        )
        goto(WelcomeState)
    }
}

val Options = state(Interaction) {


    onResponse<BuyFruit> {
        val fruits = it.intent.fruits
        if (fruits != null) {
            goto(OrderReceived(fruits))
        }
        else {
            propagate()
        }
    }

    onResponse<DadJoke> {
        furhat.say("Excellent joke choice!")
        goto(TellingJoke(jokeListDad[Random().nextInt(jokeListDad.size)]))
    }

    onResponse<CuddlyJoke> {
        furhat.say("Excellent joke choice!")
        goto(TellingJoke(jokeListCute[Random().nextInt(jokeListCute.size)]))
    }

    onResponse<KnockJoke> {
        furhat.say("Excellent joke choice!")
        goto(TellingJoke(jokeListKnock[Random().nextInt(jokeListKnock.size)]))
    }

    onResponse<RequestOptions> {
        furhat.say("We have ${Fruit().optionsToText()}")
        furhat.ask("Do you want some?")
    }

    onResponse<Yes> {
        random(
                { furhat.ask("What kind of fruit do you want?") },
                { furhat.ask("What type of fruit?") }
        )
    }
}


val WelcomeState = state(Options) {
    onEntry {
        furhat.say("I'm the famous Furhat comedian!")
        delay(1000)
        furhat.say("To start off, I need to know a little about you!")
        delay(1000)
        goto(Question1State)
    }
}

val IdleState = state(Options) {
    onEntry {
        furhat.say("Could I ask you another question?")
        delay(2000)
        goto(Question1State)
    }
}

val Question1State = state(Options) {
    onEntry {
        random(
                { furhat.ask("Do you like glögg... sorry, I meant mulled wine?") },
                { furhat.ask("Do you believe in Santa?") }
        )
    }

    onResponse<Yes> {
        furhat.say("Interesting...")
        goto(TellingJoke(jokeListChristmas[Random().nextInt(jokeListChristmas.size)]))
    }

    onResponse<No> {
        furhat.say("Excellent, neither do I.")
        goto(Question2State)
    }
}

val Question2State = state(Options) {
    onEntry {
        random(
                { furhat.ask("Have you got a poor sense of humour?") },
                { furhat.ask("Do you like bad jokes?") }
        )
    }

    onResponse<Yes> {
        furhat.say("Great! So do I!")
        goto(TellingJoke(jokeListDad[Random().nextInt(jokeListDad.size)]))
    }

    onResponse<No> {
        furhat.say("Phew, you had me worried there.")
        goto(Question3State)
    }
}

val Question3State = state(Options) {
    onEntry {
        random(
                { furhat.ask("Do you like penguins with furry hats?") },
                { furhat.ask("Should cupcakes have big eyes?") }
        )
    }

    onResponse<Yes> {
        furhat.say("Nice! I love animals.")
        goto(TellingJoke(jokeListCute[Random().nextInt(jokeListCute.size)]))
    }

    onResponse<No> {
        furhat.say("Such a shame, but that's the best, I can't pet them after all.")
        goto(TellingJoke(jokeListKnock[Random().nextInt(jokeListKnock.size)]))
    }
}

val TakingOrder = state(Options) {
    onEntry {
        random(
            { furhat.ask("How about some fruits?") },
            { furhat.ask("Do you want some fruits?") }
        )
    }

    onResponse<No> {
        furhat.say("Okay, that's a shame. Have a splendid day!")
        goto(Idle)
    }
}


fun OrderReceived(fruits: FruitList) : State = state(Options) {
    onEntry {
        furhat.say("${fruits.text}, what a lovely choice!")
        fruits.list.forEach {
            users.current.order.fruits.list.add(it)
        }
        furhat.ask("Anything else?")
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<No> {
        furhat.say("Okay, here is your order of ${users.current.order.fruits}. Have a great day!")
    }
}


fun TellingJoke(joke: Joke) : State = state(Options) {
    onEntry {
        for (i in joke.lines.indices) {
            val (pauseTime, jokeLine) = joke.nextLine()
            furhat.say(jokeLine)
            delay(pauseTime)
        }
//        random(
//                { furhat.say("HA, HA!") },
//                { furhat.say("GESTURE_LAUGH_2") },
//                { furhat.say("GESTURE_LAUGH_3") },
//                { furhat.say("GESTURE_LAUGH_4") },
//                { furhat.say("GESTURE_HA_HA_SARCASTIC") },
//                { furhat.say("GESTURE_CLEAR_THROAT") }
//        )
       goto(IdleState)
    }

    onResponse<No> {
        furhat.say("Are you not amused? Do not interrupt me, buzzkill")
    }
}



// Cute jokes
val jokeListCute = arrayOf(
        Joke(  arrayOf(
                "What does homework stand for?",
                "Half of my energy wasted on random knowledge."
        ), arrayOf(
                1000,
                3000
        )
        ),
        Joke(  arrayOf(
                "Why did the dog sit in the shade?",
                "Because he didn't want to be a hot dog."
        ), arrayOf(
                1000,
                3000
        )
        ),
        Joke(  arrayOf(
                "Where do hamburgers dance?",
                "At a meat ball."
        ), arrayOf(
                1000,
                3000
        )
        ),
        Joke(  arrayOf(
                "What did the paper say to the pencil?",
                "You've got a really good point."
        ), arrayOf(
                1000,
                3000
        )
        ),
        Joke(  arrayOf(
                "What kind of cup can't you drink out of?",
                "A cup-cake."
        ), arrayOf(
                1000,
                3000
        )
        )
)

// Knock knock jokes
val jokeListKnock = arrayOf(
        Joke(
                arrayOf( "Knock, knock", "Merry.", "Merry Christmas!"),
                arrayOf(3000, 3000, 3000)
        ),
        Joke(
                arrayOf( "Knock, knock", "Harry.", "Harry up, it’s cold out here!"),
                arrayOf(3000, 3000, 3000)
        ),
        Joke(
                arrayOf( "Knock, knock", "Noah.", "Noah good place we can get something to eat?"),
                arrayOf(3000, 3000, 3000)
        ),
        Joke(
                arrayOf( "Knock, knock", "Annie.", "Annie body home?"),
                arrayOf(3000, 3000, 3000)
        ),
        Joke(
                arrayOf( "Knock, knock", "Broccoli.", "Silly goose, broccoli doesn't have a last name."),
                arrayOf(3000, 3000, 3000)
        )
)

// Dad jokes
val jokeListDad = arrayOf(
        Joke(
                arrayOf( "Dad, can you put my shoes on?", "No, I don't think they'll fit me.", "HA HA!"),
                arrayOf(2000, 3000, 2000)
        ),
        Joke(
                arrayOf( "Dad, did you get a haircut?", "No, I got them all cut.", "Do you get it?"),
                arrayOf(2000, 3000, 2000)
        ),
        Joke(
                arrayOf( "Want to hear a joke about construction?", "No, I'm still working on it.", "Best Joke ever!"),
                arrayOf(2000, 3000, 2000)
        ),
        Joke(
                arrayOf( "Why couldn't the bicycle stand up by itself?", "It was two tired.", "HA HA!"),
                arrayOf(2000, 3000, 2000)
        ),
        Joke(
                arrayOf( "Dad, can you put the cat out?", "I didn't know it was on fire.", "Hot stuff, huh?"),
                arrayOf(2000, 3000, 2000)
        )
)

// Christmas jokes
val jokeListChristmas = arrayOf(
        Joke(
                arrayOf( "What happens to elves when they behave naughty?", "Santa gives them the sack."),
                arrayOf(2000, 3000)
        ),
        Joke(
                arrayOf( "What kind of music do elves listen to?", "Wrap."),
                arrayOf(2000, 3000)
        ),
        Joke(
                arrayOf( "What is a snowman's favorite breakfast?", "Ice Crispies."),
                arrayOf(2000, 3000)
        ),
        Joke(
                arrayOf( "Why didn't the Skeleton go to the Christmas party?", "He had no body to go with."),
                arrayOf(2000, 3000)
        ),
        Joke(
                arrayOf( "Who hides in the bakery at Christmas?", "A Mince Spy!"),
                arrayOf(2000, 3000)
        )
)

package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.order
import furhatos.flow.kotlin.*
import furhatos.nlu.common.*
import furhatos.util.Language

val Start = state(Interaction) {
    onEntry {
        random(
            {   furhat.say("Hi there") },
            {   furhat.say("Oh, hello there") }
        )

        goto(TakingOrder)
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
        goto(TellingDadJoke)
    }

    onResponse<CuddlyJoke> {
        furhat.say("Excellent joke choice!")
        goto(TellingCuddlyJoke)
    }

    onResponse<KnockJoke> {
        furhat.say("Excellent joke choice!")
        goto(TellingKnockJoke)
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

val TellingDadJoke = state(Interaction) {
    onEntry {
        random (
                {furhat.say("When does a bad joke become a dad joke? When the punchline becomes apparent!")}
        )
        goto(Idle)
    }
}

val TellingCuddlyJoke = state(Interaction) {
    onEntry {
        random (
                {furhat.say("Cuddly Joke")}
        )
        goto(Idle)
    }
}

val TellingKnockJoke = state(Interaction) {
    onEntry {
        furhat.ask("Knock knock")
        goto(Idle)
    }

    onResponse<WhoIsThere> {
        furhat.say("Santa")
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
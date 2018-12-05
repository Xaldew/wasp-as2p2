package furhatos.app.wasp_project.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.wasp_project.nlu.*

val Start : State = state(Interaction) {

    onEntry {
        furhat.ask("Hi there. Do you like robots?")
    }

    onResponse<Yes>{
        furhat.say("I like humans.")
    }

    onResponse<No>{
        furhat.say("That's sad.")
    }
}

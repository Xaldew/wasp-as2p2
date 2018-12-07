package furhatos.app.fruitseller

import furhatos.app.fruitseller.flow.Idle
import furhatos.skills.Skill
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.CereprocVoice
import furhatos.util.Language

class FruitsellerSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}

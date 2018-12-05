package furhatos.app.wasp_project

import furhatos.app.wasp_project.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class Wasp_projectSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}

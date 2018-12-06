package furhatos.app.fruitseller.nlu

import furhatos.nlu.*
import furhatos.nlu.grammar.Grammar
import furhatos.nlu.kotlin.grammar
import furhatos.nlu.common.Number
import furhatos.util.Language

class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
                "What fruits do you have?",
                "What are the alternatives?",
                "What do you have?")
    }
}

class DadJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(  "Dad",
                        "Dad joke")
    }
}

class CuddlyJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(  "Cuddly",
                        "Cute")
    }
}

class KnockJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(  "knock knock",
                        "knock joke",
                        "who is there")
    }
}

class WhoIsThere: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(  "Who is there",
                        "who")
    }
}

class Joke(
        var lines : Array<String>,
        var pauses : Array<Long>
){
    var count = 0
    fun nextLine() : Pair<Long,String>{
        val out = Pair(pauses[count], lines[count])
        if (count>=lines.size){
            count = 0
        } else {
            count++
        }
        return out
    }
}

class FruitList : ListEntity<QuantifiedFruit>()

class QuantifiedFruit(
        var count : Number? = Number(1),
        var fruit : Fruit? = null) : ComplexEnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("@count @fruit", "@fruit")
    }

    override fun toText(): String {
        return generate("$count " + if (count?.value == 1) fruit?.value else "${fruit?.value}" + "s")
    }
}

class Fruit : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("banana", "orange", "apple", "cherimoya")
    }
}

class BuyFruit(var fruits : FruitList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@fruits", "I want @fruits", "I would like @fruits", "I want to buy @fruits")
    }
}
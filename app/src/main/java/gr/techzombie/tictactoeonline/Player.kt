package gr.techzombie.tictactoeonline

class Player {
    var name:String? = null
    var available:Boolean?=null
    constructor(name:String){
     this.name = name
        this.available = false
    }
}
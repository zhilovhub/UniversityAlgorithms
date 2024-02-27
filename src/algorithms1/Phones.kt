package algorithms1

class PhoneNode(
    var name: String? = null,
    var phones: MutableList<String>? = null,
    private var leftNode: PhoneNode? = null,
    private var rightNode: PhoneNode? = null
) {

    fun addNode(newName: String, phoneNumber: String) {
        if (name == null) {
            name = newName
            phones = mutableListOf(phoneNumber)
        } else {
            if (newName > name!!) {
                if (rightNode == null) {
                    rightNode = PhoneNode(newName, mutableListOf(phoneNumber))
                } else {
                    rightNode?.addNode(newName, phoneNumber)
                }
            } else if (newName < name!!) {
                if (leftNode == null) {
                    leftNode = PhoneNode(newName, mutableListOf(phoneNumber))
                } else {
                    leftNode?.addNode(newName, phoneNumber)
                }
            } else {
                phones?.add(phoneNumber)
            }
        }
    }

    fun getTreeHeight(): Int {
        return maxOf(leftNode?.getTreeHeight() ?: 0, rightNode?.getTreeHeight() ?: 0) + 1
    }

    fun findElement(targetName: String): PhoneNode? {
        if (targetName == name) {
            return this
        }
        if (targetName > name!!) {
            return rightNode?.findElement(targetName)
        }
        return leftNode?.findElement(targetName)
    }

    fun walkThroughTree(treeNumbers: MutableList<PhoneNode?> = mutableListOf(), walkType: String): List<PhoneNode?> {
        when (walkType) {
            "прямой" -> {
                treeNumbers.add(this)
                leftNode?.walkThroughTree(treeNumbers, walkType)
                rightNode?.walkThroughTree(treeNumbers, walkType)
            }
            "обратный" -> {
                leftNode?.walkThroughTree(treeNumbers, walkType)
                rightNode?.walkThroughTree(treeNumbers, walkType)
                treeNumbers.add(this)
            }
            "симметричный" -> {
                leftNode?.walkThroughTree(treeNumbers, walkType)
                treeNumbers.add(this)
                rightNode?.walkThroughTree(treeNumbers, walkType)
            }
        }

        return treeNumbers
    }

    fun deleteValue(targetName: String) {
        if (name == targetName) {  // deleting root
            if (leftNode == null && rightNode == null) {
                name = null
                phones = null
            } else if (leftNode == null) {
                name = rightNode?.name
                phones = rightNode?.phones
                leftNode = rightNode?.leftNode
                rightNode = rightNode?.rightNode
            } else if (rightNode == null) {
                name = leftNode?.name
                phones = leftNode?.phones
                leftNode = leftNode?.rightNode
                rightNode = leftNode?.leftNode
            } else {
                val theSmallestNode = rightNode!!.walkThroughTree(walkType = "обратный")[0]
                theSmallestNode?.name?.let {
                    val nodeParentOfTheSmallestNode = findElementThatChildEquals(it)?.first
                    if (nodeParentOfTheSmallestNode?.leftNode == null) {
                        nodeParentOfTheSmallestNode?.rightNode = theSmallestNode.rightNode
                    } else if (nodeParentOfTheSmallestNode.rightNode == null) {
                        nodeParentOfTheSmallestNode.leftNode = null
                    } else {
                        nodeParentOfTheSmallestNode.rightNode = theSmallestNode.rightNode
                        nodeParentOfTheSmallestNode.name = theSmallestNode.name
                        nodeParentOfTheSmallestNode.phones = theSmallestNode.phones
                    }
                }
                name = theSmallestNode?.name
                phones = theSmallestNode?.phones
            }
        }
        else {
            val pair = findElementThatChildEquals(targetName)
            if (pair == null) {
                println("Такого элемента нет")
            } else {
                val (node, isLeft) = pair
                if (isLeft) {
                    if (node?.leftNode?.rightNode != null) {
                        node.leftNode = node.leftNode?.rightNode
                    } else {
                        node?.leftNode = node?.leftNode?.leftNode
                    }
                } else {
                    if (node?.rightNode?.leftNode != null) {
                        node.rightNode = node.rightNode?.leftNode
                    } else {
                        node?.rightNode = node?.rightNode?.rightNode
                    }
                }
            }
        }
    }

    private fun findElementThatChildEquals(targetName: String): Pair<PhoneNode?, Boolean>? {
        if (leftNode?.name == targetName) {
            return Pair(this, true)
        }
        if (rightNode?.name == targetName) {
            return Pair(this, false)
        }
        if (targetName > name!!) {
            return rightNode?.findElementThatChildEquals(targetName)
        }
        return leftNode?.findElementThatChildEquals(targetName)
    }
}


fun main() {
    print("Введите количество записей: ")
    val n = readLine()!!.toInt()
    val tree = PhoneNode()

    for (i in 1..n) {
        print("$i. Введи имя и номер телефона в формате 'имя номер' (без кавычек): ")
        val (name, phoneNumber) = readLine()!!.split(" ")
        tree.addNode(name, phoneNumber)
    }

    println(tree.walkThroughTree(walkType = "симметричный").map { "${it?.name} - ${it?.phones}" })
    println(tree.findElement("Илья"))
    println(tree.findElement("Егор"))
    tree.deleteValue("Илья")

    println("После удаления:")
    println(tree.walkThroughTree(walkType = "симметричный").map { "${it?.name} - ${it?.phones}" })
}

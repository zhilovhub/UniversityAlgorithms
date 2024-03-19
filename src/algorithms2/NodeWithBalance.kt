package algorithms2

import java.lang.Integer.max

class NodeWithBalance(
    var nodeValue: Int? = null,
    private var leftNode: NodeWithBalance? = null,
    private var rightNode: NodeWithBalance? = null,
    private var parent: NodeWithBalance? = null,
    var balance: Int = 0,
    var height: Int = 0
) {

    private fun updateHeight() {
        height = max(rightNode?.height ?: -1, leftNode?.height ?: -1) + 1
    }

    private fun updateBalance() {
        balance = (rightNode?.height ?: -1) - (leftNode?.height ?: -1)
    }

    fun addNode(newNodeValue: Int) {
        if (nodeValue == null) {
            nodeValue = newNodeValue
        } else {
            if (newNodeValue > nodeValue!!) {
                if (rightNode == null) {
                    rightNode = NodeWithBalance(newNodeValue, parent = this)
                } else {
                    rightNode?.addNode(newNodeValue)
                }
            } else {
                if (leftNode == null) {
                    leftNode = NodeWithBalance(newNodeValue, parent = this)
                } else {
                    leftNode?.addNode(newNodeValue)
                }
            }
        }
        updateHeight()
        updateBalance()
    }

    fun getTreeHeight(): Int {
        return maxOf(leftNode?.getTreeHeight() ?: 0, rightNode?.getTreeHeight() ?: 0) + 1
    }

    fun findElement(targetValue: Int): NodeWithBalance? {
        if (targetValue == nodeValue) {
            return this
        }
        if (targetValue > nodeValue!!) {
            return rightNode?.findElement(targetValue)
        }
        return leftNode?.findElement(targetValue)
    }

    fun walkThroughTree(treeNumbers: MutableList<NodeWithBalance?> = mutableListOf(), walkType: String): List<NodeWithBalance?> {
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

    fun deleteValue(targetValue: Int) {
        if (nodeValue == targetValue) {  // deleting root
            if (leftNode == null && rightNode == null) {
                nodeValue = null
            } else if (leftNode == null) {
                nodeValue = rightNode?.nodeValue
                leftNode = rightNode?.leftNode
                rightNode = rightNode?.rightNode
            } else if (rightNode == null) {
                nodeValue = leftNode?.nodeValue
                leftNode = leftNode?.rightNode
                rightNode = leftNode?.leftNode
            } else {
                val theSmallestNode = rightNode!!.walkThroughTree(walkType = "обратный")[0]
                theSmallestNode?.nodeValue?.let {
                    val nodeParentOfTheSmallestNode = findElementThatChildEquals(it)?.first
                    if (nodeParentOfTheSmallestNode?.leftNode == null) {
                        nodeParentOfTheSmallestNode?.rightNode = theSmallestNode.rightNode
                    } else if (nodeParentOfTheSmallestNode.rightNode == null) {
                        nodeParentOfTheSmallestNode.leftNode = null
                    } else {
                        nodeParentOfTheSmallestNode.rightNode = theSmallestNode.rightNode
                        nodeParentOfTheSmallestNode.nodeValue = theSmallestNode.nodeValue
                    }
                }
                nodeValue = theSmallestNode?.nodeValue
            }
        } else {
            val pair = findElementThatChildEquals(targetValue)
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

    private fun findElementThatChildEquals(targetValue: Int): Pair<NodeWithBalance?, Boolean>? {
        if (leftNode?.nodeValue == targetValue) {
            return Pair(this, true)
        }
        if (rightNode?.nodeValue == targetValue) {
            return Pair(this, false)
        }
        if (targetValue > nodeValue!!) {
            return rightNode?.findElementThatChildEquals(targetValue)
        }
        return leftNode?.findElementThatChildEquals(targetValue)
    }
}


fun main() {
//    val n = 5
//    val numbers = (0 until n).map { Random.nextInt(0, n + 1) }
    val numbers = listOf(4, 5, 3, 2, 10, 11)
    println("$numbers - Числа")

    val tree = NodeWithBalance()
    for (i in numbers) {
        tree.addNode(i)
    }

    print(tree.walkThroughTree(walkType = "прямой").map {
        "(${Pair("Значение узла ${it?.nodeValue}", "Баланс ${it?.balance}")})"
    })
    println(" - Прямой обход")

}

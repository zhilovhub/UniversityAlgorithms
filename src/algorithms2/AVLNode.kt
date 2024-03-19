package algorithms2

import java.lang.Integer.max

class AVLNode(
    var nodeValue: Int? = null,
    private var leftNode: AVLNode? = null,
    private var rightNode: AVLNode? = null,
    private var parent: AVLNode? = null,
    var balance: Int = 0,
    var height: Int = 0
) {

    private fun updateHeight() {
        height = max(rightNode?.height ?: -1, leftNode?.height ?: -1) + 1
    }

    private fun balance() {
        if (balance == -2) {
            if (leftNode?.balance == 1) {
                leftNode?.leftRotate()
            }
            rightRotate()
        } else if (balance == 2) {
            if (rightNode?.balance == -1) {
                rightNode?.rightRotate()
            }
            leftRotate()
        }
    }

    private fun updateBalance() {
        balance = (rightNode?.height ?: -1) - (leftNode?.height ?: -1)
    }

    private fun swapValues(node1: AVLNode?, node2: AVLNode?) {
        val temp = node1?.nodeValue
        node1?.nodeValue = node2?.nodeValue
        node2?.nodeValue = temp
    }

    private fun rightRotate() {
        swapValues(this, leftNode)
        val temp = this.rightNode
        this.rightNode = this.leftNode
        this.leftNode = this.rightNode?.leftNode
        this.rightNode?.leftNode = this.rightNode?.rightNode
        this.rightNode?.rightNode = temp
        this.rightNode?.updateHeight()
        this.rightNode?.updateBalance()
        updateHeight()
        updateBalance()
    }

    private fun leftRotate() {
        swapValues(this, rightNode)
        val temp = this.leftNode
        this.leftNode = this.rightNode
        this.rightNode = this.leftNode?.rightNode
        this.leftNode?.rightNode = this.leftNode?.leftNode
        this.leftNode?.leftNode = temp
        this.leftNode?.updateHeight()
        this.leftNode?.updateBalance()
        updateHeight()
        updateBalance()
    }

    fun addNode(newNodeValue: Int) {
        if (nodeValue == null) {
            nodeValue = newNodeValue
        } else {
            if (newNodeValue > nodeValue!!) {
                if (rightNode == null) {
                    rightNode = AVLNode(newNodeValue, parent = this)
                } else {
                    rightNode?.addNode(newNodeValue)
                }
            } else {
                if (leftNode == null) {
                    leftNode = AVLNode(newNodeValue, parent = this)
                } else {
                    leftNode?.addNode(newNodeValue)
                }
            }
        }
        updateHeight()
        updateBalance()
        balance()
    }

    fun getTreeHeight(): Int {
        return maxOf(leftNode?.getTreeHeight() ?: 0, rightNode?.getTreeHeight() ?: 0) + 1
    }

    fun findElement(targetValue: Int): AVLNode? {
        if (targetValue == nodeValue) {
            return this
        }
        if (targetValue > nodeValue!!) {
            return rightNode?.findElement(targetValue)
        }
        return leftNode?.findElement(targetValue)
    }

    fun walkThroughTree(treeNumbers: MutableList<AVLNode?> = mutableListOf(), walkType: String): List<AVLNode?> {
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

    fun deleteValue(targetValue: Int): AVLNode? {
        if (nodeValue != targetValue && leftNode == null && rightNode == null) {
            println("Такого элемента нет")
            return null
        }
        else if (leftNode )
        if (nodeValue == targetValue) {
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
                val theSmallestNode = rightNode!!.getSmallest()
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
        }
    }

    private fun getSmallest(): AVLNode {
        return leftNode?.getSmallest() ?: this
    }

    private fun findElementThatChildEquals(targetValue: Int): Pair<AVLNode?, Boolean>? {
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

    val tree = AVLNode()
    for (i in numbers) {
        tree.addNode(i)
    }

    print(tree.walkThroughTree(walkType = "прямой").map {
        "(${Pair("Значение узла ${it?.nodeValue}", "Баланс ${it?.balance}")})"
    })
    println(" - Прямой обход")

}

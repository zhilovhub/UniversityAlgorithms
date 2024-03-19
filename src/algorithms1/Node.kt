package algorithms1

import kotlin.random.Random

class Node(
    var nodeValue: Int? = null,
    private var leftNode: Node? = null,
    private var rightNode: Node? = null
) {

    fun addNode(newNodeValue: Int) {
        if (nodeValue == null) {
            nodeValue = newNodeValue
        } else {
            if (newNodeValue > nodeValue!!) {
                if (rightNode == null) {
                    rightNode = Node(newNodeValue)
                } else {
                    rightNode?.addNode(newNodeValue)
                }
            } else {
                if (leftNode == null) {
                    leftNode = Node(newNodeValue)
                } else {
                    leftNode?.addNode(newNodeValue)
                }
            }
        }
    }

    fun getTreeHeight(): Int {
        return maxOf(leftNode?.getTreeHeight() ?: 0, rightNode?.getTreeHeight() ?: 0) + 1
    }

    fun findElement(targetValue: Int): Node? {
        if (targetValue == nodeValue) {
            return this
        }
        if (targetValue > nodeValue!!) {
            return rightNode?.findElement(targetValue)
        }
        return leftNode?.findElement(targetValue)
    }

    fun walkThroughTree(treeNumbers: MutableList<Node?> = mutableListOf(), walkType: String): List<Node?> {
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

    private fun findElementThatChildEquals(targetValue: Int): Pair<Node?, Boolean>? {
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
    val n = 5
    val numbers = listOf(4, 5, 3, 2, 10, 11)
    println(numbers)

    val tree = Node()
    for (i in numbers) {
        tree.addNode(i)
    }

    println(tree.walkThroughTree(walkType = "симметричный").map { it?.nodeValue })
    println(tree.walkThroughTree(walkType = "обратный").map { it?.nodeValue })
    println(tree.walkThroughTree(walkType = "прямой").map { it?.nodeValue })

    println(tree.getTreeHeight())
    println(tree.findElement(7))
    tree.deleteValue(4)

    // for check that node deleted
    println(tree.walkThroughTree(walkType = "симметричный").map { it?.nodeValue })
}

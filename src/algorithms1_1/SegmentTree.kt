package algorithms1_1

class SegmentNode(
    var keyMin: Int,
    var keyMax: Int,
    private var leftNode: SegmentNode? = null,
    private var rightNode: SegmentNode? = null,
    var summ: Int = 0
) {

    init {
        if (keyMax - keyMin > 1) {
            leftNode = buildTree(keyMin, (keyMin + keyMax) / 2)
            rightNode = buildTree((keyMin + keyMax) / 2, keyMax)
            summ = leftNode!!.summ + rightNode!!.summ
        } else {
            summ = keyMin
        }
    }

    fun printTree(tree: MutableList<SegmentNode> = mutableListOf()): List<SegmentNode> {
        leftNode?.printTree(tree)
        tree.add(this)
        rightNode?.printTree(tree)

        return tree
    }

    fun searchX(target: Int, count: Int = 0): Int {
        if (target in keyMin until keyMax) {
            if (keyMax - keyMin == 1) {
                return 1
            }
            return 1 + leftNode!!.searchX(target, count + 1) + rightNode!!.searchX(target, count + 1)
        }
        return 0
    }

    fun searchSum(l: Int, r: Int, acc: Int = 0): Int {
        var result = acc
        if (keyMax > r) {
            result += leftNode!!.searchSum(l, r, acc=acc)
        } else if (keyMin < l) {
            result += rightNode!!.searchSum(l, r, acc=acc)
        }
        return acc + summ
    }

    companion object {
        fun buildTree(l: Int, r: Int): SegmentNode {
            return SegmentNode(keyMin = l, keyMax = r)
        }
    }
}


fun main() {
    print("Enter l and r: ")
    val (l, r) = readLine()!!.split(" ").map { it.toInt() }
    val tree = SegmentNode.buildTree(l, r)

    // симметричный обход
    println(tree.printTree().map { Pair(it.keyMin, it.keyMax) })

    // подсчет X
    println(tree.searchX(2))

    // подсчет суммы
    println(tree.searchSum(3, 6))
}
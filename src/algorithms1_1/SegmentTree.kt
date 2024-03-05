package algorithms1_1

class SegmentNode(
    private var keyMin: Int? = null,
    private var keyMax: Int? = null,
    private var leftNode: SegmentNode? = null,
    private var rightNode: SegmentNode? = null
) {

    companion object {
        fun buildTree(l: Int, r: Int): SegmentNode {
            val node = SegmentNode()

            keyMin = l
            keyMax = r

            leftNode = SegmentNode().buildTree(l, (l + r) / 2)
            right

            return this
        }
    }
}
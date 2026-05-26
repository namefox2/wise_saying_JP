import SwiftUI

struct FuriganaText: View {
    let segments: [Segment]
    var fontSize: CGFloat = 20
    var furiganaSize: CGFloat = 10
    var color: Color = .primary

    var body: some View {
        FlowLayout(spacing: 2) {
            ForEach(Array(segments.enumerated()), id: \.offset) { _, seg in
                if seg.hasReading {
                    VStack(spacing: 1) {
                        Text(seg.reading)
                            .font(.system(size: furiganaSize))
                            .foregroundColor(color.opacity(0.65))
                        Text(seg.text)
                            .font(.system(size: fontSize))
                            .foregroundColor(color)
                    }
                    .fixedSize()
                } else {
                    Text(seg.text)
                        .font(.system(size: fontSize))
                        .foregroundColor(color)
                        .padding(.top, furiganaSize + 1)
                        .fixedSize()
                }
            }
        }
    }
}

// MARK: - Flow layout

struct FlowLayout: Layout {
    var spacing: CGFloat = 4

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        layout(subviews: subviews, width: proposal.width ?? 0).size
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        let result = layout(subviews: subviews, width: bounds.width)
        for (i, frame) in result.frames.enumerated() {
            subviews[i].place(
                at: CGPoint(x: bounds.minX + frame.minX, y: bounds.minY + frame.minY),
                proposal: ProposedViewSize(frame.size)
            )
        }
    }

    private func layout(subviews: Subviews, width: CGFloat) -> (size: CGSize, frames: [CGRect]) {
        var frames: [CGRect] = []
        var x: CGFloat = 0, y: CGFloat = 0, rowH: CGFloat = 0

        for subview in subviews {
            let sz = subview.sizeThatFits(.unspecified)
            if x + sz.width > width, x > 0 {
                y += rowH + spacing
                x = 0; rowH = 0
            }
            frames.append(CGRect(origin: .init(x: x, y: y), size: sz))
            rowH = max(rowH, sz.height)
            x += sz.width + spacing
        }
        return (CGSize(width: width, height: y + rowH), frames)
    }
}

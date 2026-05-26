import SwiftUI

enum RevealStep: Int, Comparable {
    case kanji    = 0
    case furigana = 1
    case korean   = 2

    static func < (lhs: RevealStep, rhs: RevealStep) -> Bool { lhs.rawValue < rhs.rawValue }
}

struct StepBlock: View {
    let quote: Quote
    @Binding var step: RevealStep
    var fontScale: Double = 1.0
    let colors: AppColors

    var body: some View {
        VStack(spacing: 20) {
            // Kanji — always visible
            Text(quote.kanji)
                .font(.system(size: 22 * fontScale, weight: .medium))
                .foregroundColor(colors.text)
                .multilineTextAlignment(.center)
                .padding(.horizontal)

            // Furigana step
            if step >= .furigana {
                FuriganaText(
                    segments: quote.segments,
                    fontSize: 18 * fontScale,
                    color: colors.textSecondary
                )
                .multilineTextAlignment(.center)
                .padding(.horizontal)
                .transition(.opacity.combined(with: .move(edge: .top)))
            } else {
                revealButton(label: "후리가나 보기") {
                    withAnimation(.easeInOut(duration: 0.25)) { step = .furigana }
                }
            }

            // Korean step
            if step >= .korean {
                Text(quote.korean)
                    .font(.system(size: 16 * fontScale))
                    .foregroundColor(colors.textSecondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                    .transition(.opacity.combined(with: .move(edge: .top)))
            } else if step >= .furigana {
                revealButton(label: "한국어 보기") {
                    withAnimation(.easeInOut(duration: 0.25)) { step = .korean }
                }
            }
        }
        .animation(.easeInOut(duration: 0.25), value: step)
    }

    private func revealButton(label: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            Text(label)
                .font(.caption)
                .foregroundColor(colors.accent)
                .padding(.horizontal, 14)
                .padding(.vertical, 7)
                .background(colors.accent.opacity(0.15))
                .clipShape(Capsule())
        }
    }
}

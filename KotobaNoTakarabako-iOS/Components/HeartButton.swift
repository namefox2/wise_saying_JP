import SwiftUI

struct HeartButton: View {
    @Binding var isFavorite: Bool
    var activeColor: Color = .red
    var size: CGFloat = 24
    var onToggle: (() -> Void)? = nil

    var body: some View {
        Button {
            withAnimation(.spring(response: 0.3, dampingFraction: 0.6)) {
                isFavorite.toggle()
            }
            onToggle?()
        } label: {
            Image(systemName: isFavorite ? "heart.fill" : "heart")
                .font(.system(size: size))
                .foregroundColor(isFavorite ? activeColor : .gray)
                .scaleEffect(isFavorite ? 1.2 : 1.0)
                .animation(.spring(response: 0.3, dampingFraction: 0.6), value: isFavorite)
        }
        .buttonStyle(.plain)
    }
}

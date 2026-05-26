import SwiftUI
import SwiftData

struct QuoteCardView: View {
    @Environment(\.appColors) var colors
    @Environment(DataStoreManager.self) var dataStore
    @Environment(\.modelContext) var modelContext
    @Query private var favoriteItems: [FavoriteItem]

    let category: String
    @State private var vm: QuoteViewModel

    init(category: String) {
        self.category = category
        let ds = DataStoreManager.shared
        _vm = State(wrappedValue: QuoteViewModel(
            quotes: QuoteRepository.shared.quotes(for: category),
            autoPlay: ds.autoPlay,
            autoBlur: ds.autoBlur
        ))
    }

    var body: some View {
        ZStack {
            colors.background.ignoresSafeArea()

            if let quote = vm.currentQuote {
                VStack(spacing: 0) {
                    // Progress bar
                    Text(vm.progress)
                        .font(.caption)
                        .foregroundColor(colors.textTertiary)
                        .padding(.top, 8)

                    // Card — tap left/right or swipe to navigate
                    cardContent(quote)
                        .contentShape(Rectangle())
                        .gesture(swipeGesture)
                        .onTapGesture { location in
                            let half = UIScreen.main.bounds.width / 2
                            location.x < half ? vm.previous() : vm.next()
                        }
                        .frame(maxHeight: .infinity)

                    controlBar(quote: quote)
                }
            } else {
                Text("명언이 없습니다.")
                    .foregroundColor(colors.textSecondary)
            }
        }
        .navigationTitle(category)
        .navigationBarTitleDisplayMode(.inline)
    }

    // MARK: - Card

    private func cardContent(_ quote: Quote) -> some View {
        VStack(spacing: 24) {
            Spacer()

            // Category badge
            Text(quote.cat)
                .font(.caption.bold())
                .foregroundColor(colors.accent)
                .padding(.horizontal, 12).padding(.vertical, 5)
                .background(colors.accent.opacity(0.15))
                .clipShape(Capsule())

            // Step-based reveal
            StepBlock(
                quote: quote,
                step: $vm.revealStep,
                fontScale: dataStore.fontSize,
                colors: colors
            )

            // Author
            Text("— \(quote.author)")
                .font(.caption)
                .foregroundColor(colors.textTertiary)

            Spacer()
        }
        .padding(.horizontal)
    }

    // MARK: - Control bar

    private func controlBar(quote: Quote) -> some View {
        HStack(spacing: 40) {
            navButton(icon: "chevron.left.circle.fill") { vm.previous() }

            HeartButton(
                isFavorite: Binding(
                    get: { isFavorite(quote) },
                    set: { _ in toggleFavorite(quote) }
                ),
                activeColor: colors.heartActive,
                size: 28
            )

            Button { vm.toggleAutoPlay() } label: {
                Image(systemName: vm.autoPlayEnabled ? "pause.circle.fill" : "play.circle.fill")
                    .font(.title)
                    .foregroundColor(vm.autoPlayEnabled ? colors.accent : colors.textTertiary)
            }

            navButton(icon: "chevron.right.circle.fill") { vm.next() }
        }
        .padding(.vertical, 20)
        .frame(maxWidth: .infinity)
        .background(colors.surface)
    }

    private func navButton(icon: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            Image(systemName: icon)
                .font(.title)
                .foregroundColor(colors.accent)
        }
    }

    // MARK: - Gestures

    private var swipeGesture: some Gesture {
        DragGesture(minimumDistance: 30).onEnded { g in
            if g.translation.width < -30 { vm.next() }
            else if g.translation.width > 30 { vm.previous() }
        }
    }

    // MARK: - Favorites

    private func isFavorite(_ quote: Quote) -> Bool {
        favoriteItems.contains { $0.itemId == quote.id && $0.type == "quote" }
    }

    private func toggleFavorite(_ quote: Quote) {
        if isFavorite(quote) {
            favoriteItems
                .filter { $0.itemId == quote.id && $0.type == "quote" }
                .forEach { modelContext.delete($0) }
        } else {
            modelContext.insert(FavoriteItem(itemId: quote.id, type: "quote"))
        }
    }
}

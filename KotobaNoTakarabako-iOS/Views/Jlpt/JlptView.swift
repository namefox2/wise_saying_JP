import SwiftUI
import SwiftData

struct JlptView: View {
    @Environment(\.appColors) var colors
    @Environment(DataStoreManager.self) var dataStore
    @Environment(\.modelContext) var modelContext
    @Query private var favoriteItems: [FavoriteItem]

    @State private var vm = JlptViewModel()
    var initialLevel: String = "전체"

    init(initialLevel: String = "전체") {
        self.initialLevel = initialLevel
    }

    private let levelColors: [String: Color] = [
        "N1": .red, "N2": .orange, "N3": .yellow, "N4": .green, "N5": .blue
    ]

    var body: some View {
        ZStack {
            colors.background.ignoresSafeArea()

            VStack(spacing: 0) {
                levelFilter

                if let word = vm.currentWord {
                    VStack(spacing: 0) {
                        Text(vm.progress)
                            .font(.caption)
                            .foregroundColor(colors.textTertiary)
                            .padding(.top, 8)

                        wordCard(word)
                            .contentShape(Rectangle())
                            .gesture(swipeGesture)
                            .frame(maxHeight: .infinity)

                        controlBar(word: word)
                    }
                } else {
                    Spacer()
                    Text("단어가 없습니다.")
                        .foregroundColor(colors.textSecondary)
                    Spacer()
                }
            }
        }
        .navigationTitle("JLPT 단어")
        .navigationBarTitleDisplayMode(.inline)
        .onAppear { vm.selectLevel(initialLevel) }
    }

    // MARK: - Level filter

    private var levelFilter: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 8) {
                ForEach(vm.levels, id: \.self) { level in
                    let isSelected = vm.selectedLevel == level
                    let col = levelColors[level] ?? colors.accent
                    Button { vm.selectLevel(level) } label: {
                        Text(level)
                            .font(.subheadline.bold())
                            .foregroundColor(isSelected ? .white : col)
                            .padding(.horizontal, 16).padding(.vertical, 8)
                            .background(isSelected ? col : Color.clear)
                            .clipShape(Capsule())
                            .overlay(Capsule().stroke(col, lineWidth: 1))
                    }
                }
            }
            .padding(.horizontal).padding(.vertical, 8)
        }
        .background(colors.surface)
    }

    // MARK: - Word card

    private func wordCard(_ word: Word) -> some View {
        VStack(spacing: 20) {
            Spacer()

            // Badges row
            HStack(spacing: 8) {
                badge(word.level, bg: levelColors[word.level] ?? colors.accent, fg: .white)
                badge(word.pos, bg: colors.accent.opacity(0.15), fg: colors.accent)
                Spacer()
                HeartButton(
                    isFavorite: Binding(
                        get: { isFavorite(word) },
                        set: { _ in toggleFavorite(word) }
                    ),
                    activeColor: colors.heartActive,
                    size: 24
                )
            }
            .padding(.horizontal)

            // Main word
            VStack(spacing: 8) {
                Text(word.kanji)
                    .font(.system(size: 40 * dataStore.fontSize, weight: .bold))
                    .foregroundColor(colors.text)
                Text(word.reading)
                    .font(.system(size: 18 * dataStore.fontSize))
                    .foregroundColor(colors.textSecondary)
                Text(word.meaning)
                    .font(.system(size: 16 * dataStore.fontSize))
                    .foregroundColor(colors.accent)
                    .padding(.top, 4)
            }

            // Example sentence
            VStack(alignment: .leading, spacing: 8) {
                Text("예문")
                    .font(.caption.bold())
                    .foregroundColor(colors.textTertiary)
                Text(word.exampleKanji)
                    .font(.system(size: 15 * dataStore.fontSize))
                    .foregroundColor(colors.text)
                Text(word.exampleReading)
                    .font(.system(size: 13 * dataStore.fontSize))
                    .foregroundColor(colors.textSecondary)
                Text(word.exampleKorean)
                    .font(.system(size: 13 * dataStore.fontSize))
                    .foregroundColor(colors.textTertiary)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding()
            .background(colors.surface)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .padding(.horizontal)

            Spacer()
        }
    }

    private func badge(_ text: String, bg: Color, fg: Color) -> some View {
        Text(text)
            .font(.caption.bold())
            .foregroundColor(fg)
            .padding(.horizontal, 10).padding(.vertical, 4)
            .background(bg)
            .clipShape(Capsule())
    }

    // MARK: - Control bar

    private func controlBar(word: Word) -> some View {
        HStack {
            Button { vm.previous() } label: {
                Image(systemName: "chevron.left.circle.fill")
                    .font(.title).foregroundColor(colors.accent)
            }
            Spacer()
            Button { vm.next() } label: {
                Image(systemName: "chevron.right.circle.fill")
                    .font(.title).foregroundColor(colors.accent)
            }
        }
        .padding(.horizontal, 48).padding(.vertical, 20)
        .background(colors.surface)
    }

    private var swipeGesture: some Gesture {
        DragGesture(minimumDistance: 30).onEnded { g in
            if g.translation.width < -30 { vm.next() }
            else if g.translation.width > 30 { vm.previous() }
        }
    }

    // MARK: - Favorites

    private func isFavorite(_ word: Word) -> Bool {
        favoriteItems.contains { $0.itemId == word.id && $0.type == "word" }
    }

    private func toggleFavorite(_ word: Word) {
        if isFavorite(word) {
            favoriteItems
                .filter { $0.itemId == word.id && $0.type == "word" }
                .forEach { modelContext.delete($0) }
        } else {
            modelContext.insert(FavoriteItem(itemId: word.id, type: "word"))
        }
    }
}

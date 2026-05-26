import SwiftUI
import SwiftData

struct FavoritesView: View {
    @Environment(\.appColors) var colors
    @Environment(\.modelContext) var modelContext
    @Query private var favoriteItems: [FavoriteItem]
    @State private var vm = FavoritesViewModel()
    @State private var showShareSheet = false
    @State private var shareText = ""

    var body: some View {
        NavigationStack {
            ZStack {
                colors.background.ignoresSafeArea()

                VStack(spacing: 0) {
                    let quotes = vm.favoriteQuotes(from: favoriteItems)
                    let words  = vm.favoriteWords(from: favoriteItems)

                    Picker("", selection: $vm.selectedTab) {
                        Text("명언 (\(quotes.count))").tag(0)
                        Text("단어 (\(words.count))").tag(1)
                    }
                    .pickerStyle(.segmented)
                    .padding()

                    if vm.selectedTab == 0 { quotesList(quotes) }
                    else                   { wordsList(words) }
                }
            }
            .navigationTitle("즐겨찾기")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        let q = vm.favoriteQuotes(from: favoriteItems)
                        let w = vm.favoriteWords(from: favoriteItems)
                        shareText = vm.exportText(quotes: q, words: w)
                        showShareSheet = true
                    } label: {
                        Image(systemName: "square.and.arrow.up")
                    }
                }
            }
            .sheet(isPresented: $showShareSheet) {
                ShareSheet(text: shareText)
            }
        }
    }

    // MARK: - Quotes list

    private func quotesList(_ quotes: [Quote]) -> some View {
        Group {
            if quotes.isEmpty { emptyState(icon: "heart.slash", message: "즐겨찾기한 명언이 없습니다") }
            else {
                List {
                    ForEach(quotes) { q in
                        quoteRow(q).listRowBackground(colors.surface)
                    }
                }
                .scrollContentBackground(.hidden)
            }
        }
    }

    private func quoteRow(_ quote: Quote) -> some View {
        HStack(alignment: .top, spacing: 12) {
            VStack(alignment: .leading, spacing: 6) {
                Text(quote.cat)
                    .font(.caption.bold()).foregroundColor(colors.accent)
                    .padding(.horizontal, 8).padding(.vertical, 2)
                    .background(colors.accent.opacity(0.15)).clipShape(Capsule())
                Text(quote.kanji)
                    .font(.subheadline).foregroundColor(colors.text).lineLimit(2)
                Text(quote.korean)
                    .font(.caption).foregroundColor(colors.textSecondary).lineLimit(2)
            }
            Spacer()
            Button { removeQuote(quote) } label: {
                Image(systemName: "heart.fill").foregroundColor(colors.heartActive)
            }
        }
        .padding(.vertical, 4)
    }

    // MARK: - Words list

    private func wordsList(_ words: [Word]) -> some View {
        Group {
            if words.isEmpty { emptyState(icon: "heart.slash", message: "즐겨찾기한 단어가 없습니다") }
            else {
                List {
                    ForEach(words) { w in
                        wordRow(w).listRowBackground(colors.surface)
                    }
                }
                .scrollContentBackground(.hidden)
            }
        }
    }

    private func wordRow(_ word: Word) -> some View {
        HStack {
            VStack(alignment: .leading, spacing: 4) {
                HStack(spacing: 6) {
                    Text(word.level)
                        .font(.caption.bold()).foregroundColor(.white)
                        .padding(.horizontal, 6).padding(.vertical, 2)
                        .background(Color.blue).clipShape(Capsule())
                    Text(word.kanji)
                        .font(.subheadline.bold()).foregroundColor(colors.text)
                }
                Text(word.reading).font(.caption).foregroundColor(colors.textSecondary)
                Text(word.meaning).font(.caption).foregroundColor(colors.accent)
            }
            Spacer()
            Button { removeWord(word) } label: {
                Image(systemName: "heart.fill").foregroundColor(colors.heartActive)
            }
        }
        .padding(.vertical, 4)
    }

    // MARK: - Helpers

    private func emptyState(icon: String, message: String) -> some View {
        VStack(spacing: 16) {
            Spacer()
            Image(systemName: icon).font(.system(size: 48)).foregroundColor(colors.textTertiary)
            Text(message).foregroundColor(colors.textSecondary)
            Spacer()
        }
    }

    private func removeQuote(_ quote: Quote) {
        favoriteItems.filter { $0.itemId == quote.id && $0.type == "quote" }
            .forEach { modelContext.delete($0) }
    }

    private func removeWord(_ word: Word) {
        favoriteItems.filter { $0.itemId == word.id && $0.type == "word" }
            .forEach { modelContext.delete($0) }
    }
}

// MARK: - Share sheet

struct ShareSheet: UIViewControllerRepresentable {
    let text: String
    func makeUIViewController(context: Context) -> UIActivityViewController {
        UIActivityViewController(activityItems: [text], applicationActivities: nil)
    }
    func updateUIViewController(_ vc: UIActivityViewController, context: Context) {}
}

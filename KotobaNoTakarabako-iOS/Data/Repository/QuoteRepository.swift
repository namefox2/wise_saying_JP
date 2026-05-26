import Foundation
import Observation

@Observable
class QuoteRepository {
    static let shared = QuoteRepository()

    private(set) var quotes: [Quote] = []
    private(set) var isLoading = false
    private(set) var error: String?

    private var cacheURL: URL? {
        FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first?
            .appendingPathComponent("quotes.json")
    }

    private init() { load() }

    private func load() {
        if let url = Bundle.main.url(forResource: "quotes", withExtension: "json", subdirectory: "data") {
            decode(from: url)
        } else if let cache = cacheURL, FileManager.default.fileExists(atPath: cache.path) {
            decode(from: cache)
        }
    }

    private func decode(from url: URL) {
        do {
            quotes = try JSONDecoder().decode([Quote].self, from: Data(contentsOf: url))
        } catch {
            self.error = error.localizedDescription
        }
    }

    func refresh(from urlString: String) async {
        guard let url = URL(string: urlString) else { return }
        isLoading = true
        defer { isLoading = false }
        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            quotes = try JSONDecoder().decode([Quote].self, from: data)
            try? data.write(to: cacheURL ?? url)
        } catch {
            self.error = error.localizedDescription
        }
    }

    func quotes(for category: String) -> [Quote] {
        category == "전체" ? quotes : quotes.filter { $0.cat == category }
    }

    func todayQuote() -> Quote? {
        guard !quotes.isEmpty else { return nil }
        let day = Calendar.current.ordinality(of: .day, in: .year, for: Date()) ?? 1
        return quotes[day % quotes.count]
    }
}

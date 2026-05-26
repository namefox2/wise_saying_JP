import SwiftUI
import Observation

@Observable
class QuoteViewModel {
    var quotes: [Quote]
    var currentIndex: Int = 0
    var revealStep: RevealStep = .kanji
    var autoPlayEnabled: Bool

    private var autoPlayTimer: Timer?
    private var autoBlurTimer: Timer?
    private let autoBlurEnabled: Bool

    var currentQuote: Quote? { quotes.isEmpty ? nil : quotes[currentIndex] }
    var progress: String { "\(currentIndex + 1) / \(quotes.count)" }

    init(quotes: [Quote], autoPlay: Bool, autoBlur: Bool) {
        self.quotes         = quotes
        self.autoPlayEnabled = autoPlay
        self.autoBlurEnabled = autoBlur
        if autoPlay { startAutoPlay() }
        if autoBlur { scheduleAutoBlur() }
    }

    func next() {
        guard !quotes.isEmpty else { return }
        currentIndex = (currentIndex + 1) % quotes.count
        onCardChange()
    }

    func previous() {
        guard !quotes.isEmpty else { return }
        currentIndex = (currentIndex - 1 + quotes.count) % quotes.count
        onCardChange()
    }

    func toggleAutoPlay() {
        autoPlayEnabled.toggle()
        autoPlayEnabled ? startAutoPlay() : stopAutoPlay()
    }

    private func onCardChange() {
        revealStep = .kanji
        if autoBlurEnabled { scheduleAutoBlur() }
    }

    private func startAutoPlay() {
        stopAutoPlay()
        autoPlayTimer = Timer.scheduledTimer(withTimeInterval: 5, repeats: true) { [weak self] _ in
            self?.next()
        }
    }

    private func stopAutoPlay() {
        autoPlayTimer?.invalidate()
        autoPlayTimer = nil
    }

    private func scheduleAutoBlur() {
        autoBlurTimer?.invalidate()
        autoBlurTimer = Timer.scheduledTimer(withTimeInterval: 3, repeats: false) { [weak self] _ in
            withAnimation { self?.revealStep = .furigana }
            self?.autoBlurTimer = Timer.scheduledTimer(withTimeInterval: 3, repeats: false) { [weak self] _ in
                withAnimation { self?.revealStep = .korean }
            }
        }
    }

    deinit {
        stopAutoPlay()
        autoBlurTimer?.invalidate()
    }
}

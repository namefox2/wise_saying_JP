import Foundation

struct Segment: Codable, Hashable {
    let text: String
    let reading: String

    var hasReading: Bool { !reading.isEmpty }
}

struct Quote: Codable, Identifiable, Hashable {
    let id: String
    let cat: String
    let kanji: String
    let segments: [Segment]
    let korean: String
    let author: String
}

import Foundation

struct Word: Codable, Identifiable, Hashable {
    let id: String
    let level: String
    let pos: String
    let kanji: String
    let reading: String
    let meaning: String
    let exampleKanji: String
    let exampleReading: String
    let exampleKorean: String
}

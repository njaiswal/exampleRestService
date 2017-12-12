// SERVICES
marvelApp.service('marvelService', ['$resource' , '$log', function ($resource, $log) {

    this.getCharacterIds = function () {
        var charactersApi = $resource("/api/characters");
        return charactersApi.query();
    };

    this.getCharacter = function (id) {
        $log.info("Info about marvel character id requested: " + id);
        var charactersInfoApi = $resource("/api/characters/" + id);
        return charactersInfoApi.get();
    };

    this.getCharacterPowers = function (id, lang) {
        var powersApi = $resource("/api/characters/" + id + "/powers");
        return powersApi.get({language: lang});
    }
}]);
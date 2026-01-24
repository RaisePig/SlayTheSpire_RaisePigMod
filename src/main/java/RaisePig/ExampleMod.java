package RaisePig;

import RaisePig.characters.MyCharacter;
import RaisePig.relics.RaisePigBook;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;

import java.nio.charset.StandardCharsets;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.MY_CHARACTER;
import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

@SpireInitializer
public class ExampleMod implements EditCardsSubscriber, EditStringsSubscriber,
        EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber {
    private static final String MY_CHARACTER_BUTTON = "RaisePigResources/img/char/Character_Button.png";
    private static final String MY_CHARACTER_PORTRAIT = "RaisePigResources/img/char/Character_Portrait.png";
    private static final String BG_ATTACK_512 = "RaisePigResources/img/512/bg_attack_512.png";
    private static final String BG_POWER_512 = "RaisePigResources/img/512/bg_power_512.png";
    private static final String BG_SKILL_512 = "RaisePigResources/img/512/bg_skill_512.png";
    private static final String SMALL_ORB = "RaisePigResources/img/char/small_orb.png";
    private static final String BG_ATTACK_1024 = "RaisePigResources/img/1024/bg_attack.png";
    private static final String BG_POWER_1024 = "RaisePigResources/img/1024/bg_power.png";
    private static final String BG_SKILL_1024 = "RaisePigResources/img/1024/bg_skill.png";
    private static final String BIG_ORB = "RaisePigResources/img/char/card_orb.png";
    private static final String ENEYGY_ORB = "RaisePigResources/img/char/cost_orb.png";
    public static final Color MY_COLOR = new Color(241.0f/255.0f,214/255.0f,229/255.0f,1.0f);

    public ExampleMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(RaisePigPink, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512,BG_SKILL_512,BG_POWER_512,ENEYGY_ORB,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,BIG_ORB,SMALL_ORB);
    }

    public static void initialize() {
        new ExampleMod();
    }

    @Override
    public void receiveEditCards() {
        // 自动导入 common 包中的卡牌
        new AutoAdd("RaisePig")
                .packageFilter("RaisePig.card.common")
                .setDefaultSeen(true)
                .cards();
        
        // 自动导入 uncommon 包中的卡牌
        new AutoAdd("RaisePig")
                .packageFilter("RaisePig.card.uncommon")
                .setDefaultSeen(true)
                .cards();
        
        // 自动导入 rare 包中的卡牌
        new AutoAdd("RaisePig")
                .packageFilter("RaisePig.card.rare")
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new MyCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, MY_CHARACTER);
    }

    public void receiveEditStrings() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "RaisePigResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "RaisePigResources/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "RaisePigResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "RaisePigResources/localization/" + lang + "/powers.json");
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new RaisePigBook(), RaisePigPink);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "eng";
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "zhs";
        }
        String json = Gdx.files.internal("RaisePigResources/localization/ZHS/keywords_" + lang + ".json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json,Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword("raisepig", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
}

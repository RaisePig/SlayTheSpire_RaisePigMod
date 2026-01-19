package RaisePig;

import RaisePig.card.*;
import RaisePig.characters.MyCharacter;
import RaisePig.powers.FeedPower;
import RaisePig.relics.MyRelic;
import RaisePig.relics.RaisePigBook;
import basemod.BaseMod;
import basemod.devcommands.kill.Kill;
import basemod.helpers.KeywordColorInfo;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;

import java.nio.charset.StandardCharsets;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.MY_CHARACTER;
import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

@SpireInitializer
public class ExampleMod implements EditCardsSubscriber, EditStringsSubscriber,
        EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber { // 实现接口
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "RaisePigResources/img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "RaisePigResources/img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "RaisePigResources/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "RaisePigResources/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "RaisePigResources/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "RaisePigResources/img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "RaisePigResources/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "RaisePigResources/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "RaisePigResources/img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "RaisePigResources/img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENEYGY_ORB = "RaisePigResources/img/char/cost_orb.png";
    public static final Color MY_COLOR = new Color(241.0f/255.0f,214/255.0f,229/255.0f,1.0f);

    public ExampleMod() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
        // 这里注册颜色
        BaseMod.addColor(RaisePigPink, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512,BG_SKILL_512,BG_POWER_512,ENEYGY_ORB,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,BIG_ORB,SMALL_ORB);
    }

    public static void initialize() {
        new ExampleMod();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数
    @Override
    public void receiveEditCards() {
        // TODO 这里写添加你卡牌的代码
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new BuildupStrike());
        BaseMod.addCard(new BuildupDefend());
        BaseMod.addCard(new SlapAndDate());
        BaseMod.addCard(new RaisePigPassword());
        BaseMod.addCard(new DoubleSlapAndDate());
        BaseMod.addCard(new TripleSlapAndDate());
        BaseMod.addCard(new PigDeadAndFixSty());
        BaseMod.addCard(new SnackBribery());
        BaseMod.addCard(new Reclamation());
        BaseMod.addCard(new AutumnHarvestUprising());
        BaseMod.addCard(new FinancialManagement());
        BaseMod.addCard(new IQuit());
        BaseMod.addCard(new FatPigArchway());
        BaseMod.addCard(new KillPigDay());
        BaseMod.addCard(new DevelopIsImportant());
        BaseMod.addCard(new PigCharge());
        BaseMod.addCard(new PigNoseBump());
        BaseMod.addCard(new FeedSplash());
        BaseMod.addCard(new CutChives());
        BaseMod.addCard(new PigstyReinforce());
        BaseMod.addCard(new FeedLoan());
        BaseMod.addCard(new TestPower1());
        BaseMod.addCard(new TestPower2());
        BaseMod.addCard(new TestPower3());
    }

    // 当开始添加人物时，调用这个方法
    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new MyCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, MY_CHARACTER);
    }

    public void receiveEditStrings() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "RaisePigResources/localization/" + lang + "/cards.json"); // 加载相应语言的卡牌本地化内容。
        // 如果是中文，加载的就是"ExampleResources/localization/ZHS/cards.json"
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "RaisePigResources/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "RaisePigResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "RaisePigResources/localization/" + lang + "/powers.json");
    }

    @Override
    public void receiveEditRelics() {
        // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        // addRelic用于向共享池中添加遗物，addRelicToCustomPool用于向自定义角色池中添加遗物
//        BaseMod.addRelic(new MyRelic(), RelicType.SHARED);
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
                //The modID here must be lowercase
                BaseMod.addKeyword("RaisePig".toLowerCase(), keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
}



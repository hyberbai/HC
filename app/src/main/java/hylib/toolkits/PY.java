package hylib.toolkits;

import java.nio.charset.Charset;

public class PY {
//    // 二级字库
//    private static String a = "吖锕嗄锿捱嗳霭砹嗌嫒暧瑷桉庵谙鹌埯铵揞犴黯坳嗷廒獒遨聱螯鳌鏖媪岙骜鏊Ａ";
//    private static String b = "岜粑茇菝魃鲅灞掰捭呗瘢癍阪坂钣舨浜蒡勹孢煲龅鸨葆褓趵陂鹎邶悖碚蓓褙鞴鐾贲锛畚坌嘣甏荸匕吡妣秕俾舭畀哔荜狴铋婢庳萆弼愎筚滗裨跸箅嬖篦薜濞髀璧襞砭笾煸蝙鳊窆匾碥褊弁忭汴苄缏飑髟骠瘭镖飙飚镳婊裱鳔蹩傧缤槟镔豳殡膑髌鬓邴禀摒饽啵孛亳钹鹁踣礴跛簸擘檗逋钸晡醭卟钚瓿玢宀疒８Ｂ";
//    private static String c = "嚓礤骖黪粲璨伧嘈漕艚螬艹恻岑涔噌杈馇锸猹槎檫衩镲汊姹钗侪虿瘥觇婵孱禅廛潺镡蟾躔谄蒇冁忏羼伥娼菖阊鲳苌徜嫦昶惝氅怅鬯怊焯晁耖砗屮坼抻琛嗔宸谌碜龀榇谶柽蛏铛瞠丞枨埕铖裎塍酲哧蚩鸱眵笞嗤媸螭魑茌墀踟篪豉褫彳叱饬敕啻傺瘛忡茺舂憧艟铳瘳俦帱惆雠樗刍蜍蹰杵楮褚亍怵绌憷黜搋啜嘬踹巛氚舡遄舛钏怆陲棰槌蝽莼鹑踔辍龊呲祠茈鹚糍苁枞骢璁淙琮楱腠辏徂殂猝蔟蹙蹴汆Ｃ";
//    private static String d = "哒耷嗒褡妲怛笪靼鞑岱甙绐迨玳埭黛眈聃殚瘅箪儋疸啖萏澹裆谠凼宕砀菪叨忉氘纛锝噔簦戥嶝磴镫羝嘀镝籴荻觌氐诋邸坻柢砥骶娣谛棣睇碲嗲巅癫踮阽坫玷钿癜簟貂鲷铞垤瓞堞揲耋牒蹀鲽仃玎疔耵酊啶腚碇铥咚岽氡鸫垌峒胨胴硐蔸篼蚪窦嘟渎椟牍黩髑笃芏蠹椴煅簖怼碓憝镦礅盹趸沌炖砘咄裰铎踱哚缍沲卩亻赕铫町铤夂丶Ｄ";
//    private static String e = "屙莪锇婀呃苊轭垩谔阏愕萼腭锷鹗颚噩鳄蒽摁鸸鲕迩珥铒佴嗯唔诶２Ｅ";
//    private static String f = "垡砝幡蕃燔蹯蘩畈梵邡枋钫鲂舫妃绯扉蜚霏鲱淝腓悱斐榧翡篚狒痱镄棼鼢偾鲼瀵沣砜葑酆唪俸缶呋趺麸稃跗凫孚芙芾怫绂绋苻祓罘茯郛砩莩蚨匐桴艴菔蜉幞蝠黻拊滏黼驸鲋赙蝮鳆馥攵犭Ｆ";
//    private static String g = "旮伽钆尜尕尬陔垓赅丐戤坩泔苷疳酐尴澉橄擀旰矸绀淦罡筻戆槔睾杲缟槁藁诰郜锆圪纥袼鬲嗝塥搿膈镉骼哿舸虼硌哏亘艮茛赓哽绠鲠肱蚣觥珙佝缑篝鞲岣枸笱诟媾彀遘觏轱鸪菰蛄觚酤毂鹘汩诂牯罟钴嘏臌瞽崮梏牿痼锢鲴胍鸹呱卦诖倌鳏掼涫盥鹳咣桄胱犷妫皈鲑宄庋匦晷簋刿炅鳜衮绲磙鲧呙埚崞聒蝈帼掴虢馘猓椁蜾桧莞呷Ｇ";
//    private static String h = "鹄铪嗨胲醢顸蚶鼾邗晗焓菡颔撖瀚绗颃沆蒿嚆薅蚝嗥濠昊皓颢灏诃嗬劾曷盍颌阖翮壑桁珩蘅訇薨闳泓荭蕻黉讧瘊篌糇骺後逅堠鲎虍烀轷唿惚滹囫斛猢煳鹕槲醐觳浒琥冱岵怙戽祜笏扈瓠鹱骅铧桦踝獾洹萑锾寰缳鬟奂浣逭漶鲩擐肓隍徨湟遑潢璜篁癀蟥鳇诙咴虺晖珲麾隳洄茴哕浍荟恚彗喙缋蕙蟪阍馄诨溷耠锪劐攉钬夥镬嚯藿蠖砉圜Ｈ";
//    private static String i = "Ｉ";
//    private static String j = "丌叽乩玑芨矶咭剞唧屐笄嵇犄赍跻畿齑墼羁岌亟佶笈戢殛楫蒺瘠蕺虮掎戟嵴麂芰哜洎觊偈暨跽霁鲚稷鲫髻骥迦浃痂笳袈葭跏镓岬郏恝戛铗蛱胛瘕戋菅湔犍搛缣蒹鲣鹣鞯囝枧笕趼睑裥锏谫戬翦謇蹇牮谏楗毽腱僭踺茳豇缰礓耩洚绛犟糨艽姣茭蛟跤僬鲛鹪佼挢皎敫徼噍醮疖嗟孑讦诘拮桀婕颉碣鲒羯蚧骱钅矜衿卺堇廑馑槿瑾妗荩赆缙觐噤泾旌菁腈阱刭肼儆憬弪迳胫婧獍扃迥鸠赳阄啾鬏柩桕噘孓珏崛桷觖厥劂谲獗蕨橛镢蹶矍爝皲筠麇捃纟廴鄄莒９Ｊ";
//    private static String k = "蚵咔佧胩锎剀垲恺铠蒈锴忾龛戡侃莰阚瞰伉闶钪尻栲铐犒珂轲疴钶颏稞窠瞌蝌髁岢恪氪骒缂嗑溘锞裉铿倥崆箜芤叩筘蔻刳堀骷绔喾侉蒯郐哙狯脍髋诓哐诳夼邝圹纩贶悝逵馗喹揆暌睽蝰夔跬匮喟愦蒉篑聩琨锟髡醌鲲悃阃栝蛞Ｋ";
//    private static String l = "冫靓邋旯砬剌瘌崃徕涞铼赉睐濑癞籁岚褴斓镧榄漤罱啷莨稂锒螂阆蒗唠崂痨铹醪栳铑耢仂叻泐鳓嫘缧檑羸耒诔酹嘞塄愣骊喱鹂缡蓠蜊嫠鲡罹藜黧蠡俚娌逦锂澧醴鳢呖坜苈戾枥疠俪栎疬轹郦猁砺莅唳笠粝蛎詈跞雳溧篥奁裢鲢濂臁蠊琏裣蔹娈殓楝潋椋墚踉魉嘹寮獠缭鹩钌蓼咧冽洌埒捩趔躐鬣啉粼嶙遴辚瞵麟廪懔檩蔺膦躏囹泠苓柃瓴棂绫翎聆蛉鲮酃呤熘浏旒遛骝镏鎏绺锍鹨泷茏栊珑胧砻癃泸０６Ｌ";
//    private static String m = "膂褛栾鸾脔銮锊囵捋猡脶椤镙倮瘰蠃泺荦珞摞漯雒呒嬷蟆犸杩唛霾荬劢颟鞔鳗螨墁幔缦熳镘邙硭漭蟒牦旄蛑髦蝥蟊峁泖茆昴耄袤瑁瞀懋莓嵋湄猸楣镅鹛浼袂魅扪钔焖懑虻甍瞢朦礞艨勐艋蜢懵蠓咪祢猕縻麋蘼芈弭敉脒糸汨宓谧嘧沔黾眄湎腼喵鹋杪眇淼缈邈咩篾蠛岷玟苠珉缗闵泯愍鳘茗冥溟暝瞑酩缪谟嫫馍麽殁茉秣蓦貊瘼镆貘耱哞侔眸鍪毪仫沐坶苜钼渑Ｍ";
//    private static String n = "拗廾乜镎肭衲捺艿柰萘鼐囡喃楠赧腩蝻囔馕曩攮孬呶硇铙猱蛲垴瑙讷坭怩铌猊鲵伲旎昵睨鲇鲶黏辇廿埝茑袅嬲脲陧臬嗫颞蹑蘖咛聍佞甯妞忸狃侬哝耨孥驽弩胬钕恧衄傩喏搦锘恁Ｎ";
//    private static String o = "噢讴瓯耦怄Ｏ";
//    private static String p = "钯拚彷冖葩杷筢俳哌蒎爿蹒蟠泮袢襻滂逄螃脬庖狍匏疱醅锫帔旆辔霈湓怦嘭堋蟛丕纰邳铍噼芘枇蚍郫陴埤罴蜱貔鼙庀仳圮擗癖淠媲睥甓犏翩骈胼蹁谝剽缥螵殍瞟嘌嫖氕丿苤姘嫔颦榀牝娉俜枰鲆钋鄱皤叵钷笸珀掊裒攴噗匍璞濮镤溥氆镨蹼Ｐ";
//    private static String q = "匚袷湫峤趄瞿桤萋嘁槭蹊亓圻岐芪耆颀淇萁骐琦琪祺蛴綦蜞蕲鳍麒屺芑杞绮綮汔荠葺碛憩葜髂阡芊佥岍悭愆骞搴褰钤虔掮箝肷慊缱芡茜倩椠戕戗跄蜣锖锵镪嫱樯羟襁炝硗跷劁缲荞谯憔鞒樵愀诮妾挈惬箧锲衾芩嗪溱噙檎螓锓吣揿圊蜻鲭檠黥苘謦箐磬罄跫銎邛穹茕筇蛩蚯楸鳅犰虬俅逑赇巯遒裘蝤鼽糗岖诎祛蛐麴黢劬朐鸲蕖磲璩蘧氍癯衢蠼阒觑悛诠荃辁铨筌蜷鬈畎绻悫阕阙逡郄７Ｑ";
//    private static String r = "镕蚺髯苒禳穰荛桡娆荏稔仞轫饪衽肜狨嵘榕蝾糅蹂鞣铷嚅濡薷襦颥洳溽缛蓐朊蕤芮枘蚋睿偌箬Ｒ";
//    private static String s = "灬杓丨凵葚仨卅飒脎噻毵糁馓搡磉颡缫臊鳋埽瘙啬铯穑铩痧裟鲨唼歃霎彡芟姗钐埏舢跚潸膻讪疝骟鄯嬗蟮鳝殇觞熵垧绱筲艄蛸劭潲猞畲佘厍滠麝诜哂矧谂渖胂椹蜃笙眚晟嵊蓍酾鲺饣炻埘莳鲥豕礻贳舐轼铈弑谥筮螫艏狩绶殳纾姝倏菽摅毹秫塾沭腧澍唰蟀闩涮孀氵妁铄嗍搠蒴槊厶咝鸶缌蛳厮锶澌汜兕姒祀泗驷俟笥耜忪凇崧淞菘嵩悚竦嗖溲馊飕锼螋叟嗾瞍薮稣夙涑谡嗉愫蔌觫簌狻荽眭睢濉谇莘３４Ｓ";
//    private static String t = "沓呔焘钭冂苕扌趿铊溻鳎闼遢榻骀邰炱跆鲐薹肽钛昙郯覃锬忐钽铴羰镗饧溏瑭樘螗螳醣帑傥耥韬饕洮啕鼗忑忒铽慝滕绨缇鹈醍倜悌逖裼畋阗忝殄掭佻祧笤龆蜩髫鲦窕粜萜餮莛婷葶蜓霆梃嗵仝佟茼砼僮潼恸骰荼酴钍堍菟抟疃彖煺暾饨豚氽乇佗坨沱柁砣跎酡橐鼍庹柝箨Ｔ";
//    private static String u = "Ｕ";
//    private static String v = "Ｖ";
//    private static String w = "亠娲佤腽崴剜蜿纨芄绾脘菀琬畹罔惘辋魍偎逶隈葳煨薇囗帏沩闱涠帷嵬炜玮洧娓诿隗猥痿艉韪鲔軎猬阌雯刎汶璺蓊蕹倭莴喔肟幄渥硪龌圬邬浯蜈鼯仵妩庑忤怃迕牾鹉兀阢杌芴焐婺痦骛寤鹜鋈５Ｗ";
//    private static String x = "郇彐噱荨圩兮穸郗唏奚浠欷淅菥粞翕舾皙僖蜥嬉樨歙熹羲螅蟋醯曦鼷觋隰玺徙葸屣蓰禧饩阋舄禊狎柙硖遐瑕黠罅氙祆籼莶跹酰暹娴痫鹇冼猃蚬筅跣藓燹岘苋霰芗缃葙骧庠饷飨鲞蟓枭哓枵骁绡逍潇箫魈崤筱偕勰撷缬绁亵渫榍榭廨獬薤邂燮瀣躞昕歆馨鑫囟陉硎擤荇悻芎咻庥鸺貅馐髹岫溴盱胥顼诩栩糈醑洫勖溆煦蓿谖揎萱暄煊儇痃漩璇泫炫铉渲楦碹镟泶踅鳕谑埙窨獯薰曛醺峋恂洵浔荀鲟徇巽Ｘ";
//    private static String y = "肀剡桠伢岈琊睚痖迓垭娅砑氩揠恹胭崦菸湮腌鄢嫣讠闫妍芫筵檐兖俨偃厣郾琰罨魇鼹晏焱滟酽谳餍赝泱鞅炀徉烊蛘怏恙幺夭吆爻肴轺珧徭繇鳐杳窈崾鹞曜揶铘邺晔烨谒靥衤咿猗欹漪噫黟圯诒怡迤饴咦荑贻眙酏痍嶷钇苡舣旖弋刈仡佚呓佾峄怿驿奕弈羿轶悒挹埸翊缢瘗蜴熠镒劓殪薏翳癔镱懿洇氤铟喑堙垠狺鄞夤龈霪吲蚓瘾茚胤莺瑛嘤撄罂璎鹦膺茔荥萦楹滢蓥潆嬴瀛郢颍瘿媵唷邕墉慵壅镛鳙狳谀馀萸雩嵛揄腴瑜觎窬蝓伛俣圄圉庾瘐窳龉聿妪饫昱钰谕阈鹆煜蓣毓蜮燠鹬鬻鸢眢箢沅爰鼋塬橼螈垸媛掾瑗刖钺樾龠瀹纭芸昀氲狁殒郓恽愠韫熨攸禺１Ｙ";
//    private static String z = "辶酢喋阝咂拶甾崽糌簪昝趱錾瓒臧驵奘唣迮啧帻笮舴箦赜仄昃谮缯罾锃甑吒哳揸楂齄砟咤痄蚱砦瘵旃谵搌鄣嫜獐璋蟑仉嶂幛钊啁诏笊棹蜇辄谪摺磔赭褶柘鹧浈桢祯蓁榛箴胗轸畛缜稹圳鸩朕赈峥钲铮筝徵诤卮栀祗胝埴絷跖摭踯芷祉咫枳轵黹酯忮豸帙郅栉陟桎贽轾鸷彘痣蛭骘雉膣觯踬舯锺螽冢踵妯碡纣绉胄荮酎籀侏邾洙茱铢槠潴橥竺舳瘃躅渚麈伫苎杼炷疰箸翥颛啭馔丬隹骓惴缒肫窀倬涿斫菹镞俎躜缵攥蕞樽鳟撙阼怍祚胙唑秭梓Ｚ";
//    private static String[] L2 = new String[] { a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z };
//
//    private static char[] L2Table = CreateL2Table();

    private static String PYConfig =
      "19968.YD.Q3.WZ2SXJBY.GC.ZQPS.QBYCDSC3.D.LY.2S.GY2Z2.F.C.L.D.WDWZ.LJP3.N.J.TMY.ZWZHFL.2PQ.G.CY2.NJQY2X4.S2.J6.ML.R10.Q7.L.YZ.SECY" +
      "K.YHQWJ2.G.YX3.J2WK.JHYCHM.XJTL3.QB.X4.RD3.YSRLDZJPC.Z2J.RC2.L.CZSTZFX3.TRQ.Y.DLY2.S.Y2M3.Y.ZP.W2J3.R.F.F.Q7.KY2.WJ2FX5.ZY2HY2.S" +
      "WC.Y.SCLC2.WZ3.BG.N.B.L.S.S.SG5.D2.D6.WD2ZY.T.H.TS2YFZGNTN2.YWQ.K.P2.L2.YB2.JE.J4.TJ.YS3.KZ3.CK.L.S.Z4.M2.D3.G.Y2.X2.L.JZCQKC.N." +
      "WH5.Q9.B3.CEQ4.J3.ZQL6.SF.L.PBYS.X.Y2.C.Y2L2.JXF.J.P4.F.A3.B13.GB.S2.D.J3.THY.T2.J.C3.KJ2.QN.ZWM11.Z.Z.Q4.Y3.2J3.R.WP5.X4.Z.T8.J" +
      "16.OT6.FCK4.F2.L4.B21.D.T.BCN2.C5.A7.CS19.X6.X3.L12.S4.2JT4.S.J5.P10.JX3.D6.R14.L29.EWY.YXCZ.XG.K.M3.D2.TSY3.D.D5.J2.R2.Q2.BGLX." +
      "LG.GXBQJDZ.YJS2.JCT2.N2.GR2.CZ4.M2.MPR.X.JN3.G.Z.YM5.LD2.FBHCJ.KYL3.DXL2.J3.Q.ZS.L.DL2.J.C9.L.N2.JF2.F6.F.P.KH2.D.SX.TACJDH.Z2D." +
      "R2.FQY.K2.CW2.XH3.Y2LZGC.C2.S3.P3.PL.B.JG.D2.K2.ZSQSCK.G.GKDJT6.X.LQ2.GJ2.T.P5.W.J2.Y3.B.J.SJ4.F2.G10.P.J.QJ5.P7.H2.Y7.L.QBGJWML" +
      "4.DZNJQS3.LJL10.HS3.B3.Y.M.X4.M.L3.X.K6.M4.Q11.X8.BS3.GWY4.BC.X4.P.PF4.BHB.CQ2.Z2.JK.X2.G3.F3.K10.PQY2.BN.S.Q.S.SWHB3.HX.B2Z.DMN" +
      "2.B.B.2BZKL.L.GW.D.W2.ZMYW.JQL.JX.J4.Q2.C.ETL2.L.2YS7.C2.L.H4.Y2.XY.JX.CJ4.S7.S4.Q3.X.SC5.YCJYSF3.F2.S.QSBX.P3.SD2.KGJL.DKZJZBDK" +
      "TCSY.PYHST.LDJ3.Y.CG.Y.HJD.TMHLTXZX.LAM3.J.LTYFB.Q2.FBDF.HTKSQ.ZY.WC2.XC.WH.W.Y2.E2.D.C.GF4.N.MYTOLBYG.QW5.N.L3.Z8.GCW.HNGP2.SHM" +
      "2.J.Z.D.P4.ZH.JYF.Z.KGK2.LDN.S5.GZ.YLZ2MZYJ.K.Z.KH3.X2.X.Y.YAPS.H.DWHZ2.PXAGKYDXBH.H.KD.JNMY5.GOCSLN2.KX3.2Z6.B.HG.G.Y4.SC.A5.XT" +
      "Z2.E6.M4.L2.ZH2.J2.F.H.SWSC.L3.Y4.S.TH.Z.KZ.S2.LA10.TD5.C4.F2.PSLZ2.P.SZN6.BDLX2.CT.J.K.WNS2.L.2HZ3.N.Y2.W3.CH2.XH.K7.X9.L.Z.M.P" +
      ".K.Y2.K5.AX3.S2.AS3.KDSC.S5.SG.J.W.SCH2.H.QNH2.E2.DA.T.A5.2S.DQ6.CJ2.P.2G8.X2.M2.LD3.BY2.M4.CP3.ZC.Z.S2.L.X3.H12.CJY5.D2.PJQ8.O." +
      "J3.QEZYS4.X4.G4.SP8.RH7.HT3.C15.X11.H7.R4.J13.N9.N2.W2.QS.JHXYNT.D6.H.Y2.KC2.WM3.LG2.GT4.PY.Y.QYQ17.H2.T3.S4.ZXG.WGPYD2.Z5.KCQ2." +
      "J.Z.B4.J2.F.BTKHZK5.K2.JTLBWFZP2.K.T.TGPD.N.P3.A2.MK3.DC5.C2L6.XD5.LG7.D4.YFGYD.KS.E.D.YK3.K.N3.Y9.GA7.M2.CS2.L.PC2.SXG2.N.YB3.P" +
      "8.D.A4.Z3.YPJ2.S2.K.T3.DJ3.P.T3.Q3.D3.Y4.D.HB2.D5.K5.Y4.D14.L7.TC3.S2.T3.T5.S6.G5.TY17.S.CM.J.S3.Y8.SM5.QL3.ZX8.MD18.J4.B3.Y11.H" +
      "3.H14.R6.SR.Z.S2.K2.H2.Y8.D.C2.B5.F.X4.KXW2.SD.Y2.G4.YH.D.2TF.2YH.S2.T2.YKJD.K4.LH.Y2.QNF.F2.KZ.Q2.BYJTZ.X5.D.S2.A13.2N.N.JT3.H3" +
      ".SRFW.ZFM.R2.Y4.DJ2.YJ.M4.N4.B.T2.FWYG2.NZ2.D6.M.Q2.Q7.M4.S4.JGSXW2.SP.Y.JS5.J.L2.Y3.J12.C.Y3.Z.W.WLYRJLP2.L6.W4.N3.N2.JS2.D.E3." +
      "M7.YW.X.Q5.C3.E5.P2.WB10.J4.H7.B4.J2.L9.YCST2.W4.X18.M7.MY14.A7.PX.Y2.C5.G2.JS6.J2.X5.A.P.P.L3.Z3.LD.Y2.C2.N.M5.Q23.X12.BS26.N.Y" +
      "2.M8.S15.ZJ.JKY.ZCSFBZX.M2.BJGNX2.HL.N3.SC3.F4.R2.N2.BN.TGZ.YSA.SW2.H3.M.D.ZGZDWYBS.CSKXS.H3.XG4.Z2.HYXJ.CR3.KBS2.J.JYMK4.F3.M.H" +
      "Y9.QMC.G2.WL2.Z5.L.H7.CDSXD2.S.F2.S.J2.WZ4.X.S2.EGJ.C.S.GC6.Y2.Y4.G4.J2.G3.SYCKNJWNJPC2.J2.QTJW2.SPJX3.Z3.E4.S.TL.X.L8.CT.S7.YQ4" +
      ".Y.SQ5.Y3.JQ2.QC2.C.QGXALDB5.KG5.Y.XJL3.D.Y.H.MA4.D.K.M2.Y6.X6.D6.Z7.X2.QZL.E.Y2.Q2.F10.J6.2L2.KC6.Q5.C.Y4.J2.G5.X.YS.B3.ZG5.W8." +
      "ZY8.J2.SMQ11.R2.Y13.S2.W7.J13.Z22.L3.D25.Y13.D7.W13.C.CZ2.XC2.GZQJG.W2.CQ.JYSB2.X5.XJ2.BSB.SF.S3.X2.WZT2.2PT.L2ZBZD8.DZ5.XB2.C5." +
      "WC2.ZGM4.M.WF6.H7.2M5.Z2.F2.FC15.GPN.B.X.YH2Y.GP3.Z.QB2.CG3.XLW.KYDPD.MG.F.PFX4.XDZ5.T5.B.ASKYT4.Y10.2L6.JAK2.L4.C12.X.L9.J.YT2." +
      "J3.2NKBYQN3.Y.BY3.S.S.G.Y.FH2.C3.DZ4.MXH.NJ2.M.W.R7.DQ.B3.G15.X.GD.LTHZ4.Y3.SX.T.Y2.CB.BP2.ZY.C3.P.Y.CB3.WZC.JD.XHYHLH3.X.T2.L.D" +
      "PX2.C4.Y6.H.X2.YW6.Z.D4.JH5.X.BY2.D2.JR.C3T4.ZWM3.T2.ZC2.W2.Y2.SK.BZ2.C3.N2.N2.X.HKFHTSWO2C3.C2.2ZY2.N.ZPB2.H.DLS2.DY3.JPXYNGF3." +
      "Q5.C5.ZD2.Y.NXS7.L.H2.K.H2.S3.YH2.J4.H.X2.NHEKDTG.XQ2.K2.E.TYKCNY.Y3.KQ4.X2.TH4.Y.H.B3.SQ.K.WY2.H2.Y.N2.QXQ.M.FB5.J2.XD8.QC3.JW5" +
      ".H3.T2.W.H.XC.WH5.DJ2C.BQCDGD2.XZ.H2.RX5.QC4.Q.2Y3.MBY5.E4.Y4.GY2.LF.2K3.S19.Y8.C.Q.H.S2.S3.M7.T4.M4.HK7.W4.Y.K19.B2.Z5.Q8.D9.CH" +
      "Q2.J10.C6.H3.D5.X.AM5.ML.L17.N14.M9.Y6.G.G.WJXSRXCWJ2.QHQZ.QJ3.J.KJ.GD4.J.J.L4.CD2.H5.HLFSB.J3.SHF.STCZ2.PBDR3.T2.K7.K2.QZ.KMSYN" +
      "BCR2.B2.F2.P2.E.ZCJ3.C4.JB6.YSZ.TDKZ.FP3.TKLQ.HB2.P2.PT4.B3.D3.M.CYC.M2.F.ZDCMNLF.BPL.G.JTB.TNJZPZB2.N2.LJ.YLNBZ2.KSJZ.G.QS.ZK4." +
      "PZSN.CG4.ZQA4.K2.T4.W3.ZL.WTXNDJZJH2.A.NC3.Z9.YT2.W4.WJ.TK2.Z.MBHSNJ4.B8.LS.JHD3.P.L3.BJ2.A4.CJ2.2N4.X.D4.DSDP.Z.JTQ.P3.Y.J7.L.T" +
      "C.J.KTYC2.TQ.B2.LG2.ZD.C.G.Y5.Y.K2.R3.ZXMT.C3.Y7.A.YW.C5.KJ2.J4.D.Y.Y.Z4.L.QCGL2.J5.CZ2.BC2.CS6.JS.G.2S4.N3.T.BD6.Q5.X2.C.G.E2.2" +
      "SBYB.T7.B.S3.Z5.M8.C.M3.Z10.2MZ7.L.YJ.P9.CS2.SH2.Z4.Z5.C4.L2.QBC.Z4.N.X4.H3.G.L2.S7.CQ.H.Q4.PB5.S5.X.C25.P8.H8.Z5.R12.Z5.J2.NZ4." +
      "PFS.YG.G2.FZ5.G2.XM2.D2.M.J3.CA2.J.L.BC3.GS2.D4.2J3.S.Q.Z2.F15.W3.ZB3.FB.L3.D.L.HX2.Z.W2.JC.F.Z.Z.D.SX8.F3.S3.P.ZMLP4.XJ.NZ2.L3." +
      "YQ8.W.J2.RDJ2Z2.2XGLGH4.SK2.W5.Y.AZ2.K3.H.C.MH3.Y2X3.T3.Z.XY4.C.MZ4.Z.S.Y2.MNC5.Z.X2.C.H7.JS2.Y2.SX2Y2H2.W4.S.B2.W.H.C5.PJX3.Q.J" +
      "G2.Z3.L3.Z.X2.X4.K4.S4.NA5.M9.AJ5.M5.B4.X4.T26.S.XYP8.X2.N6.Y.QYG2.H.C2.M.ZTZ7.2YRP.F2.Q3.SZ.L3.W.C.Q6.M.M.WMBZ.S.Z2.PD4.J2.X2.S" +
      ".ZQ2.G.CS2.W.LX2C.S2.Z5.D.QSGT3.L2.Y2M2.H.BJ.GY.C.P4.Z.SB.J2.G2.P.W.F4.X4.Z.LR.M.GZC3.SZ.L.JC.QF.X.KP2.Z.X2JG8.T.LB10.BMGQ2R3.ZX" +
      "2.GTZ.N.D2.C3.J2.J2.KNZ.LC8.C.SZ4.Z.B2ZLD2.2L.S.S2.QL5.K3.X7.XZ7.KL3.YHG2.GZM.JHGTGWK.2A2.Z.Z.TS.H2J10.YRZD2QHGJZ10.F.TJ9.L.T.MB" +
      "8.G3.Z3.G10.S3.MWL4.S.TX2.S.F10.J.L6.M.Q.G4.B2.Z2.J.P5.T2.D10.S.CL3.K3.ZG.F4.G3.Y5.L.2Z3.J12.DQ3.M8.T2.Y3.D4.S3.C.C2.Z17.X2.J2.C" +
      "2.2L.N2.M2.X4.J2.C2.C5.KQY2.L3.P.G.L.YC5.X6.LR5.ZB10.FZ4.X6.L2.Q3.T5.G8.S.C.C.H3.G6.J3.BZ12.Q4.H10.C.J10.F12.CT6.Z.M6.X.H4.Q.Y3." +
      "Q7.ZY5.G2.Q8.T8.C.J5.X3.Z11.C10.Y3.T3.X9.Q.YL5.B8.Q8.L.CM115.QCHX3.O10.Y4.X.YQ3.K4.S2.2X.Q2.G12.X8.2ZCBWQ2.W14.D.SJ4.MCYT.DS.XSC" +
      ".P4.YL2.Z3.DJ5.B8.Y8.SOD.Y7.D.HG2.Y5.W.M.M2.DY3BP.B.M5.Z8.MH3.T5.S3.S3.J7.CP6.Q.SDM.MQPN.DXCF3.F.DQ.Y.HYAYK3.DLQ.Y.2S2.Y4.T2.TZQ" +
      "3.CH.HC5.X3.QS.H3.XSRGJCW2.T3.MGW5.T.J.B.W2.X3.QF2.QYW.YH.SC2.D2.QM2.TM4.S.P3.G.M.FO2LC.WHM.SJ2.TDH4.FY.2ZGZY4.XQ3.QB.M3.L2.HGFM" +
      "S3.F2.N.LPBQ.N2.Z.LX2.PMTY.Y.BX2L.MXPZJ2.J2.H2.Y3.YL.J3.S4.X.ZJL2.D6.J.W2.HX2.T2.EZR.X3.H.HWQP2.L.J.Q2JZ.ZC.HJL.2HNX4.ZJ.B3.X2.H" +
      "2.P2.HL3.FW4.Y2.HJ3.M5.T2.N2.XS2.Y.X2.S2.CT5.T.3L2W.HD.RJZSFG2.SY2.Y2.H2.H6.ZD3.Z2XQ3.LT4.S4.N.TC3.FS.PD2.Y.G4.YC.H2.S.C3.H.Y.TM" +
      "8.Q4.Y2.ZD.JM2.Y.2S2.Z2.Y2.QD.ZBW3.W.X.W.G2.X.K3.Y.M8.PM8.TM4.PJ.H.X2.Z3.H11.Q2.Y15.WS3.K.JX2.G5.TY7.K3.L2.M2.Y2.P.L2.X4.S.QS.X." +
      "RH2.NT.R3.CP4.D3.Z3.F.H.Z2T.B2.G3.ZYSMY.3L.BT10.D4.H8.P3.Q8.L3.LYC10.M3.L4.XYM.M.M.S.Z2.H7.Y7.YX3.L.W10.P3.Q.L3.H3.L6.TC3.S.Z3.S" +
      ".C.T7.C3.CG2.2SP13.L4.Z5.L11.A5.D6.J.L6.S7.LB11.B.HR12.P34.P8.HY7.X17.F3.Y18.G2.H14.B12.HSM.DH4.LZ.J3.Z.ZCY4.G3.LC3.Y3.C.QKD2.Z2" +
      ".WQ13.XJTPJ.T.B3.2ZD.SLC2.HSLT4.L.Y13.HL.Z3.Y4.K.FSYH.TJR.X7.W.P3.F11.YH5.W2.H.HM.TBF11.J8.C2Y4.R14.D4.X.H.J13.Y.S5.M.XZW5.Z3.BH" +
      "4.B.T2.S6.X5.X4.X4.R3.LX5.SY7.Y3.A6.M.S3.X9.R10.L5.FY10.Y4.Z8.X10.X12.B22.J12.Z.P3.YA3.JFYBD.Y.S.P7.PB3.PD4.D6.Y.N.P.M.ML3.2M.W4" +
      ".JG2.S2.Q3.TX3.WGXL2.J5.D2.J.P2.K12.J12.QF.FQ3.A.ZGMY7.YKND3.B4.PX.H.F4.G.J4.N.HJ6.RS2.DXSKZY.YB3.L.YSL4.L.X6.N3.L4.G.MCY3.M2CS2" +
      ".MH2.W3.XZMW.X2.N2.H3.MC4.HY13.J2.Z.A4.J8.L11.XT.X14.H5.X2.L.Y.W2.D2.J4.J4.M3.M2.B6.W.M2.WHX.LD3.D2.XB4.P.K6.MS2.Z.JFL7.G4.M.Z4." +
      "E.Y.H3.B4.H16.Q.2L2.LY4.LS10.C6.Z2.HQ.K.Q.W.C.Y2.LQ2P5.Q4.M19.X.Y.NH2Y.RS13.T2.G5.YA6.J2.C.L3.X3.Z2.Y13.H.P8.BCQ16.W23.Z9.G.D.H." +
      "PBR.W7.WO4.L.PC7.B4.Z8.M.B.Z.P4.GDS.T2.S5.S2.YS.F.B.NTYJS.D.ND.DH2.Z.B4.C2.F2.TJ.QW4.P4.LBZX8.LQ3.F7.S.C3.JW5.J3.T2.J8.S.YB.D.JL" +
      ".GJ2.S.NLY2.BJ5.YL.CFZ2PGKC2.DZ2.T2J3.JXZB.ZYJQ2.Y4.Y.ZHY.D2.T2.P3.LZ2.WSL.HX4.TF2.C4.B2.G2.W.C.Z.D4.H.L3.Y4.J.Y.S.C3.WJ.B.LCS2." +
      "DBT.B2.M2.C2Z2.Q3.M.2YH2.L8.AB6.Y.P5.D.L4.X7.D3.Q8.G2.D.2B3.Z.D.JHG2.G2.J2.A.H2.W2.X10.P9.P2.ZJ12.M2.Y.BZPBYBY2BHAZYJHBKBGDP2BS3" +
      "BM5BG8BMDBXMBZ3BXD2BPBD2BS2BM2BMDMBKB.11BYS4BZMBY3B.BZX3BSBM5BCKJMBTBY3B2ZB.3BD8BLJ8BYJ5B2SDBPMBN2BJC12BD3BKGRM3BMC6BKSX2B2M12BP" +
      "CBM2BPBQBZDBS3BK2BTBL5BZBGBQ13BJ9BC3BMJ5BSYBZCSCJCJ.DA4CS2CJCG4CXFKDMS6CH2CQK3CYPCYCZCDCYC2FCZ3CTCD2ZCF2CLB2CP3CP2CSZA2LTCL6CGCN" +
      "4CGCXCDCX2CSXQ5CX12CWLYMQ8CJ4CP10CDCD2CLAS2CBCD3CWDCBQ3CDCZCJCBCB8CPJDT5CX4CN6CB3CSL5CP3CZK3CG14CM3CQ5CQCD2CL2CH6CJ3CD7C6DJ10DM5" +
      "DC15DB5D2SLDSDSQ4DXDQZ9DF2D2Z2DZQHZ2SCDM2DXDTP4DJDZ8DHDQ5DBJ2DLC4DX4DF10D.12DX11DR5D2YLDQHDXSDT2DG2DBDQDZ3DKM2DB2DM6DZ3DMCDQYDZD" +
      "SDZDJC7DJ2DYDH2DXDLF2D.4DCD2S5DR2DB2DZ2DEKEC2ES15EW3EJEZED2JEG6EM10ES5ES24ER3EXEJQXQK4ECZTEQZEBEY4EQ3EYZ2ETJCJ3ECWE2K2E2FKDFX3FY" +
      ".5FY11FL11FL10FS2FZ4F2JZ2FJFTS6FJFD9F2Z2F.FG3FDJFBFJFZS3FHFX2FBJ3FSFD2FCFL3FTS2FBFD.DFZ4GJ4GPGJGLGB6GXGQGDGJQ2GFKZTGDGCGKGBSGZ2G" +
      "JGP11GS2GXS2GYGKGC4GQGJ4GB10GQ3GBJGS4GKQ4GY3GZQTLDXRJ3GX2GZ3GZ8GH3GHZP9HKHL5HGF2HG3HC3HLB3HC3HL2HL5HP4HDHM8HC3HGS2H.6HD8HD6HD3HZ" +
      "13HB6HBZL11HJ37HMD6HLXZ11HF7HBL2HPHCZ3HTLXS2H.HYZ4HF3HL2HLCJ5HC2HL.3JS3JRJHXJHJZC7JGTQJC2JM2JZK14JN8JM2JX14JW21JSJS8JZ3JL62JX8JZ" +
      "46JQ7JQ140JM5JF5JY22J36KZ24KD3K2J.HZXGYJWKJRWYKCPSGNKZLFZWF2KNSXGXFLZSX3ZBFCSYJDBRJKRK2HGXJL2JTGXJXKSTJTJXLKXQFCSGSWMSBCKLQ2ZWLZ" +
      ".KXJMLTMJKHS2DBKGZHDLBMYJFRZFKGCLYJBPMLYSMSXLSZJ2QHJZFKGKQ7KYKQX10KGW2KWH2LFLF4LGLGB5LY2Z3LZ3LSLP9LZ2LJ8LYLQLM5LG8LDXQLX2LQ2L.7L" +
      "JTLX6LG2LCYLYLW3LC4LYLY7L2X2LQ6LDCF3LZJ2LP4LHLHALY7LFY3LY2LKMZQ4LDES2LN3L.GCHYB2LS3LH4LTO.NJP6LMLELDYDS2LCLDLG2LND8LZN4LG5LPLJ14" +
      "LKC6LA12LM2Y2MSYMSZMRMLJ6MH2MXMZMDGRGMWCGZM2F3MJFZMNAKMGYMY2MQ2MFMJTS2ZXSWZMD5MBGT7MPZMZPJSZB2MH2MYXMLDK.JNYMKYGHGDMJMXMAMPN4MZ3" +
      "MC3MJ2MKSZQNMN2MBMWMJ3M.13MP2MFMTN5ML5MP7MTMJML2Y3M2FMFQW3M.D5MC4MXMSMN3MESMYJ2MY4MFXNMW2TB7MGMB4MGMB6MB2MTMX5MZ2MLMPM.8MS6MYS4M" +
      "TLBY2MY3MSMG7M15NC3NZ2NZ2NC5N2Z6NZJN.NY.CNXJY5N2S2NSNSNT6NCSNWZNCSY4NBNHF2BNZJCN2ZDBXGCN.2NX5NS2NTNWNM3NS8NSNC4NC8NMN4PGLJPSYF4P" +
      "CY2PJAN2PJPW3PMPQYPSXDPQM4PXPF2PWZPSPQ2PKJLPJ2QYFBRXJHPFW2PZYQ3PYF2PC2PB2PWLPEX2CZSPYRL2TPMQ2PKMPBGMYP.PRK4PS3PBPY2P.2PG3PF5PZMF" +
      "Q2MBCM2PCPJLPN2PYQPM2PYGQ10PJC2PMCJPFPZPJHYCP2RCPTP2QX5QJXQC5QRJYHQL5QJRB2QHQDQRHYMYXJ.YMHZY7QHB2QDTS7QLP2QL4QS3QJM4QS2QT.QGQ.7Q" +
      "LFEM4QK2LSWQXH.2YQCM2QWJ3QJQG2QJQJ2QH4QFQCQS3QCBQTBH3QX3QPD5QGLF5QYZ3QS5Q.TQB5QMPWDQH6QN3QTL6Q3YXS8QX6QY3QEL8QB10QF5QZQXSG5QPQD5" +
      "QPQHZJ3QCQWQKTQX4Q5SD4SC2KSJ.SP8SLSM7SB13SK2SP5SZ2J2SESHSZ2SX4SRW5SR2SB2SB6SLSJL2SYSYM5SP3SX11SL2SX17SMSM3SZ2SW4SCSC9SN11SQD2LKA" +
      "B5SF4S.JR11SQ2SH4SZ5SR3SJ9SQSY4SWJH2SL5SBHSW7SY11SXB7SX3SHSX5SX6SRST7SG7SJ5SCM2SXSO6SL7ST4SF15S2TZ3TH5TH11TM4TN16TQTF14TZ3TM16T2" +
      "HLNL2TQ5TX3TY3TG8TCQTJ2TS7T2HMGSXCSYM7TWRBP5TYTC6TYH5TGZ2TJFCDTX2TQ.Z2T.KH3TR5TZ3TGTQSTLGD2TLCQM2TH3TYWTZ2TKJ4TG4TQ3TZMTZJNSQ3TS" +
      "Y4TETSTFS3TZWFL2TC4TY4TWZWFWMWQ2WLMQWX3WT4WY2WP2WY2WQ3WQ2WG8WYGC2WKWX4WY3WHWB6WF2W.QM8WFWK3WHWD4WN.CR3WLPWX2WY2WSWR5WQ3WT7WM5W3X" +
      "M2XS2CXA3XTXP4XL2XZ2XS5XM3XM6XZM8XP4XP4XH4XH3XS15XC4XM6XL8XMXRH4XM5XLC15X.6XD2XQ7XN8XY9XJXY7XHQ2YB2XBCXSCXGXSXN4XZ5XRQJXYMXAN2XJ" +
      "2XDXP4XT9XW5XPXM6XB5XF5XQ4XG4XCL2XZD2XK4XC3XPX2Y2X2Q5XS2X2LKJ2XB7XDBXSP3XLG2XTX.3XG7XB5XHX2B5XBCM5XD3XR4XTC4XQ3XLXZX9YQ2YX25YBJ6" +
      "YR20YP3YX3YT2YF58YJGYGMSCL2JXD2YGJQJ3YJ3YGY.YS4YJYGC4YS3YZ3YH19YH54Y.9YL48YZ7Y8ZYT8ZS115ZJ30ZQ63ZJ5ZP51ZYJDFR2JHTRSQZXYXJZJHO.YN" +
      "XELZSFSFJZGHPZS2ZSZDZCQ2Z2YKLSGSJHCZSHDGQGXYZGXCHXZJWYQWGYHK2SEQZ.ND.FKWY2SDCLZSTZYMCDHJ2XYWEYXC.AYDMPZMDSXYBSQMJMZJMTZQLPJYQZCG" +
      "9.H4.DJ.C2.W8.S4.T6.XH7.HY7.B4.ZBC7.D2.X3.HM.M7.P3.M132.BZF.GCZXBZHZFTPBGZGEJBSTGKDMFHY2ZJH.2L2ZGJQZLSFDJ2SC2.GPDL.ZFZSZYZ.ZSYGC" +
      ".SN3.H.Z2.Z2.JFZGQ9.C2.QC4.YQ8.L10.T3.Q13.Z.ZPB2.D.F.J.ZT3.YQ.T4.BD.TJ.P4.ZF2.SB.JLG3.XJ3.K.GQK2.L3.T.J.QBXDJ.J7.X3.LY.C2.T4.C8." +
      "HJC2.TB5.CZ.Z.DZ.D3.Z3.CJ5.DPR.T2.JD.QT5.NP6.C12.B2.B2.DC.FC.D.C.J5.P2.C.Z3.Z.C7.2L3.C7.Z.X12.SG2.Q2.D7.T19.W151.CZGX.RZELRHGK.2" +
      "ZYHZLYQS2ZJ.Q.JZFLNBHGW.C.CFJ.SPYXZ.ZLXG.CB3.L4.2B.B4.CR4.Z2.B3.LD2.QY.QX.GM5.Y.YJ.YFW2.HZ.JYWLC2.T.Y2J.DEDP.DZ.S2.J3.MBJ3.Z2.T2" +
      "STPHN2.2XB.X.TZQD.T.TD2.TG.SCSZQF3.L6.HD6.KW.Y2.LB.Y2.DS.C2.Y5.B.EXHQD3.YG2.L6.TQ.Y2.A4.2Z5.LZ9.BY.X5.M2.L5.Y.D.Y.H.M.Q.K3.FXN2." +
      "B3.X.W.YHTQ.PBSB.DZYL2.Z2.Y2.QZ.X2.J3.LJKZ.Y3.X3.FGH3.JY3.LYB2.P.G.T4.C3.D4.DY3.E.J20.B4.Y3.YZ11.S.P17.L2.F2.YDQZPZYG.J4.X2.F2.Y" +
      "T2.TZHGS3.ML.C.TZXJC.TJMKSL3.YSN5.P.C.Z.CKT2.H2X7.MQ6.HT6.L2.BJX4.L5.X12.C.YS.LZYL.J10.F151.J25.Q31.M25.W117.Z43.M36.L58.A11.A10" +
      "3.B44.X25.JGYZDZPLTQCS.FDMN.C.GBT.DCZNBGBQYQJWGKFHTNPYQZQGBKP2BYZMTJDYTBLSQM.SXTBNPD.KLE3.CJYN.CTLDYK2ZX2D.HQSHDGMZSJY2CTAYRZLP." +
      "LTLKXSLZC2GEX.MFXLKJRTLQ.AQZNCMB.D2K.XGLCZJ.X.HPTD2JM.Q.KQSECQZDS.HADM2.F2MZ.GN2.2NLGBYJBRBT2M.YJDZX.CJLP.DLP.QDHL2.LYCB2.X8.C10" +
      "4.M2SY.BWCRWXHJMKMZNGWTM2.FGHKJYL.2YCXWHYECLQ.KQHT.QK.FZ.D.QW7.B3.R2.JF2YZJ4.2ZD.AT.B.FJ2LCX.LMJ2.X3.GS5.B3.Z.DY.C2.NYXP7.C.PLTX" +
      "13.YL.W2.S.H.SY3.G2.WAX2.Z10.S8.X2.H2.L2.Z4.N.QY2.XYJG.Z2.CY.C2.M2.D10.C7.2YX4.W3.L2.2L.B4.W.XJ2.X.TZPM3.HSF3.N2.L5.S.X11.Y2.A2." +
      "X.L5.BP4.M19.QL2.J2.J.D2.F.K2M2.Y3.G9.JX.B5.D8.Y5.X.A3.DQ.M3.Q7.J2.R9.B.J2.G.B49.WR.HWYTJ5.Y.YS126.YDQHXS2XWGDQBSHY2LP3J.HY.KYP." +
      "TH.YKT.EZYENMDSH.CRPQ38.F2.BS2.S2.P2B3.FS8.X36.Y2.C29.T38.T13.S.J.2TXRYCFYJSBS.YERX2.J.2B.EYN2.HXGCK.SC.M.LXJMSZNSKGX11.F2.X195." +
      "MYTXCQ.BLZ2SFJ.ZTNJYTXM.JHLHP.LCY2.J2QKZ2.CPZ.SWALQSBLC.ZJ.XG7.TJ4.DKH.H.G3.BKQ8.KB4.D.S4.G6.B.K4.M4.T3.R9.X.J7.Z4.Q6.J3.B11.H3." +
      "L11.C2.G8.YG4.KHBPM.YXL3.W.W.C2.M295.Y4.LF2.BPN3.F.BH.TGJ.WE4.2JX.2XG2LJLSTGSH.J.QL2.FKCG2NDJ.S.FD5.SEQFH2.BSAQTGY2.LBXB2MY2.GSL" +
      "Z2.L316.N2JYM.OY.BZGD.Y.Y.CQY.TSZEGX2.GMH.BLJHEYXTWQMA.BP.C3.H.EG.CMW.Y4.JH.YZ2LJ.Y3.YH.G11.L2.J4.JZ2.M6.QL9.S.L6.M13.Q3.F2.M.MH" +
      "5.H4.H3.SLN.H2.Q3.M2.DC2.YX.Q2.Q.L.DC4.A9.Z.2F.M12.Y.TD.N2.G3.T.P6.S.F12.W7.X.Y.B.QH5.Z11.QJ45.2C3.LBT.2YCQW12.LGK3.GY24431.LYE2" +
      "SWLQBJ7.ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private static char[] PYTable = LoadPYTable(PYConfig);

    public static char[] LoadPYTable(String config)
    {
        char[] arr = config.toCharArray();
        char[] table = new char[256 * 256];
        int i = 0;
        int n = 0;
        while (i < arr.length)
        {
            int i0 = i;
            while (i < arr.length && gv.IsDigit(arr[i])) i++;
            int len = i == i0 ? 1 : gv.IntVal(config.substring(i0, i));
            char chr = arr[i];
            if (chr == '.') chr = ' ';
            for (int j = n; j < n + len; j++) table[j] = chr;
            n += len;
            i++;
        }

        AddAscii(table);
        return table;
    }

    private static void AddAscii(char[] table)
    {
        for (int i = 0; i < 255; i++)
    	    table[i] = (char)i;

        for (int i = 0; i < 26; i++)
    	    table[i + 'a'] = table[i + 'A'];
    }
    
//    private static char[] CreateL2Table() {
//    	char[] table = new char[1<<16];
//        for (int i = 0; i < 26; i++)
//        	for (char c : L2[i].toCharArray())
//        		table[c] = (char)(i + 'A');
//        return table;
//	}
    
//    // 创建汉字首声母拼音查找表
//    private static char[] CreatePYTable() {
//    	char[] table = new char[1<<16];
//
//        for (int i = 0; i < 255; i++)
//        	table[i] = (char)i;
//
//        for (int i = 0; i < 26; i++)
//        {
//        	table[i + 'a'] = table[i + 'A'];
//        	// 加载L2字库
//        	for (char c : L2[i].toCharArray())
//        		table[c] = (char)(i + 'A');
//        }
//        
//        Charset charset = Charset.forName("GB18030");
//        for(int i = 0x4E00; i < 0x9FA5; i++){
//        	try {
//        		if(table[i] != 0) continue;
//    	        byte[] array = (String.valueOf((char)i)).getBytes(charset);  
//    	        if (array.length == 2)
//    	        	table[i] = FindRangePY(array[0], array[1]);
//			} catch (Exception e) {
//			}
//        }
//    	return table;
//	}
	  
    //要转换的汉字字符串  返回：拼音缩写
    public static String Get(String str)
    {
    	char[] chrs = str.toCharArray();
        for(int i = 0; i < chrs.length; i++)
        	//chrs[i] = Get(chrs[i]);
        	chrs[i] = PYTable[chrs[i]];
        return new String(chrs);
    }

    //要转换的单个汉字 返回：拼音声母
//    public static char Get(char c)
//    {
//        if (c >= 'a' && c <= 'z') return (char)(c + 'A' - 'a');
//        if ((int)c > 32 && (int)c <= 126) return c;// return "{}[]()".IndexOf(c) < 0 ? c : ' '; //字母和符号原样保留
//		try {
//	        byte[] array = (String.valueOf(c)).getBytes("GB18030");  
//	        if (array.length != 2) return ' ';
//	        char cPY = FindRangePY(array[0], array[1]);
//	        return cPY == 0 ? L2Table[c] : cPY; // 如果区间查找不到，检索二级字库的声母
//		} catch (Exception e) {
//			return ' ';
//		}
//    }
//
//    private static char FindRangePY(byte b1, byte b2)
//    {
//        int i = (b1 & 0xff) * 256 + (b2 & 0xff);
//        if (i < 0xB0A1) return 0;
//        if (i < 0xB0C5) return 'A';
//        if (i < 0xB2C1) return 'B';
//        if (i < 0xB4EE) return 'C';
//        if (i < 0xB6EA) return 'D';
//        if (i < 0xB7A2) return 'E';
//        if (i < 0xB8C1) return 'F';
//        if (i < 0xB9FE) return 'G';
//        if (i < 0xBBF7) return 'H';
//        if (i < 0xBFA6) return 'J';
//        if (i < 0xC0AC) return 'K';
//        if (i < 0xC2E8) return 'L';
//        if (i < 0xC4C3) return 'M';
//        if (i < 0xC5B6) return 'N';
//        if (i < 0xC5BE) return 'O';
//        if (i < 0xC6DA) return 'P';
//        if (i < 0xC8BB) return 'Q';
//        if (i < 0xC8F6) return 'R';
//        if (i < 0xCBFA) return 'S';
//        if (i < 0xCDDA) return 'T';
//        if (i < 0xCEF4) return 'W';
//        if (i < 0xD1B9) return 'X';
//        if (i < 0xD4D1) return 'Y';
//        if (i < 0xD7FA) return 'Z';
//        return 0;  
//    }
//    
    
    
    
    
//    
//	  private static Map<Character, String> uncommonWordsMap = null;  
//	  private static Map<String, Integer> spellMap = null; 
//
//	  static {  
//	    if (spellMap == null) {  
//	      spellMap = Collections.synchronizedMap(new LinkedHashMap<String, Integer>(396)) ;  
//	      uncommonWordsMap = Collections.synchronizedMap(new LinkedHashMap<Character, String>(200)) ;  
//	    }  
//	    initialize();  
//	    initUncommonWords();  
//	  }  
//	  
//	  /** 
//	   * 获得单个汉字的Ascii. 
//	   * @param cn 汉字字符 
//	   * @return 汉字对应的 ascii, 错误时返回 0 
//	   */ 
//	  public static int getCnAscii(char cn) {
//		  try {
//			    byte[] bytes = (String.valueOf(cn)).getBytes("GB18030");  
//			    if (bytes == null || bytes.length == 0|| bytes.length > 2 ) { // 错误  
//			      return 0;  
//			    }  
//			    if (bytes.length == 1) { // 英文字符  
//			      return bytes[0];  
//			    }  
//			    if (bytes.length == 2) { // 中文字符  
//			      int hightByte = 256 + bytes[0];  
//			      int lowByte = 256 + bytes[1];  
//			      return (256 * hightByte + lowByte) - 256 * 256; //返回 ASCII  
//			    }  
//			    return 0; // 错误  
//		} catch (Exception e) {
//		    return 0; // 错误  
//		}
//	  }  
//
//	  /** 
//	   * 初始化 
//	   */ 
//	  private static void initialize() {  
//	    spellMap.put("'a", -20319);  
//	    spellMap.put("'ai", -20317);  
//	    spellMap.put("'an", -20304);  
//	    spellMap.put("'ang", -20295);  
//	    spellMap.put("'ao", -20292);  
//	    spellMap.put("ba", -20283);  
//	    spellMap.put("bai", -20265);  
//	    spellMap.put("ban", -20257);  
//	    spellMap.put("bang", -20242);  
//	    spellMap.put("bao", -20230);  
//	    spellMap.put("bei", -20051);  
//	    spellMap.put("ben", -20036);  
//	    spellMap.put("beng", -20032);  
//	    spellMap.put("bi", -20026);  
//	    spellMap.put("bian", -20002);  
//	    spellMap.put("biao", -19990);  
//	    spellMap.put("bie", -19986);  
//	    spellMap.put("bin", -19982);  
//	    spellMap.put("bing", -19976);  
//	    spellMap.put("bo", -19805);  
//	    spellMap.put("bu", -19784);  
//	    spellMap.put("ca", -19775);  
//	    spellMap.put("cai", -19774);  
//	    spellMap.put("can", -19763);  
//	    spellMap.put("cang", -19756);  
//	    spellMap.put("cao", -19751);  
//	    spellMap.put("ce", -19746);  
//	    spellMap.put("ceng", -19741);  
//	    spellMap.put("cha", -19739);  
//	    spellMap.put("chai", -19728);  
//	    spellMap.put("chan", -19725);  
//	    spellMap.put("chang", -19715);  
//	    spellMap.put("chao", -19540);  
//	    spellMap.put("che", -19531);  
//	    spellMap.put("chen", -19525);  
//	    spellMap.put("cheng", -19515);  
//	    spellMap.put("chi", -19500);  
//	    spellMap.put("chong", -19484);  
//	    spellMap.put("chou", -19479);  
//	    spellMap.put("chu", -19467);  
//	    spellMap.put("chuai", -19289);  
//	    spellMap.put("chuan", -19288);  
//	    spellMap.put("chuang", -19281);  
//	    spellMap.put("chui", -19275);  
//	    spellMap.put("chun", -19270);  
//	    spellMap.put("chuo", -19263);  
//	    spellMap.put("ci", -19261);  
//	    spellMap.put("cong", -19249);  
//	    spellMap.put("cou", -19243);  
//	    spellMap.put("cu", -19242);  
//	    spellMap.put("cuan", -19238);  
//	    spellMap.put("cui", -19235);  
//	    spellMap.put("cun", -19227);  
//	    spellMap.put("cuo", -19224);  
//	    spellMap.put("da", -19218);  
//	    spellMap.put("dai", -19212);  
//	    spellMap.put("dan", -19038);  
//	    spellMap.put("dang", -19023);  
//	    spellMap.put("dao", -19018);  
//	    spellMap.put("de", -19006);  
//	    spellMap.put("deng", -19003);  
//	    spellMap.put("di", -18996);  
//	    spellMap.put("dian", -18977);  
//	    spellMap.put("diao", -18961);  
//	    spellMap.put("die", -18952);  
//	    spellMap.put("ding", -18783);  
//	    spellMap.put("diu", -18774);  
//	    spellMap.put("dong", -18773);  
//	    spellMap.put("dou", -18763);  
//	    spellMap.put("du", -18756);  
//	    spellMap.put("duan", -18741);  
//	    spellMap.put("dui", -18735);  
//	    spellMap.put("dun", -18731);  
//	    spellMap.put("duo", -18722);  
//	    spellMap.put("'e", -18710);  
//	    spellMap.put("'en", -18697);  
//	    spellMap.put("'er", -18696);  
//	    spellMap.put("fa", -18526);  
//	    spellMap.put("fan", -18518);  
//	    spellMap.put("fang", -18501);  
//	    spellMap.put("fei", -18490);  
//	    spellMap.put("fen", -18478);  
//	    spellMap.put("feng", -18463);  
//	    spellMap.put("fo", -18448);  
//	    spellMap.put("fou", -18447);  
//	    spellMap.put("fu", -18446);  
//	    spellMap.put("ga", -18239);  
//	    spellMap.put("gai", -18237);  
//	    spellMap.put("gan", -18231);  
//	    spellMap.put("gang", -18220);  
//	    spellMap.put("gao", -18211);  
//	    spellMap.put("ge", -18201);  
//	    spellMap.put("gei", -18184);  
//	    spellMap.put("gen", -18183);  
//	    spellMap.put("geng", -18181);  
//	    spellMap.put("gong", -18012);  
//	    spellMap.put("gou", -17997);  
//	    spellMap.put("gu", -17988);  
//	    spellMap.put("gua", -17970);  
//	    spellMap.put("guai", -17964);  
//	    spellMap.put("guan", -17961);  
//	    spellMap.put("guang", -17950);  
//	    spellMap.put("gui", -17947);  
//	    spellMap.put("gun", -17931);  
//	    spellMap.put("guo", -17928);  
//	    spellMap.put("ha", -17922);  
//	    spellMap.put("hai", -17759);  
//	    spellMap.put("han", -17752);  
//	    spellMap.put("hang", -17733);  
//	    spellMap.put("hao", -17730);  
//	    spellMap.put("he", -17721);  
//	    spellMap.put("hei", -17703);  
//	    spellMap.put("hen", -17701);  
//	    spellMap.put("heng", -17697);  
//	    spellMap.put("hong", -17692);  
//	    spellMap.put("hou", -17683);  
//	    spellMap.put("hu", -17676);  
//	    spellMap.put("hua", -17496);  
//	    spellMap.put("huai", -17487);  
//	    spellMap.put("huan", -17482);  
//	    spellMap.put("huang", -17468);  
//	    spellMap.put("hui", -17454);  
//	    spellMap.put("hun", -17433);  
//	    spellMap.put("huo", -17427);  
//	    spellMap.put("ji", -17417);  
//	    spellMap.put("jia", -17202);  
//	    spellMap.put("jian", -17185);  
//	    spellMap.put("jiang", -16983);  
//	    spellMap.put("jiao", -16970);  
//	    spellMap.put("jie", -16942);  
//	    spellMap.put("jin", -16915);  
//	    spellMap.put("jing", -16733);  
//	    spellMap.put("jiong", -16708);  
//	    spellMap.put("jiu", -16706);  
//	    spellMap.put("ju", -16689);  
//	    spellMap.put("juan", -16664);  
//	    spellMap.put("jue", -16657);  
//	    spellMap.put("jun", -16647);  
//	    spellMap.put("ka", -16474);  
//	    spellMap.put("kai", -16470);  
//	    spellMap.put("kan", -16465);  
//	    spellMap.put("kang", -16459);  
//	    spellMap.put("kao", -16452);  
//	    spellMap.put("ke", -16448);  
//	    spellMap.put("ken", -16433);  
//	    spellMap.put("keng", -16429);  
//	    spellMap.put("kong", -16427);  
//	    spellMap.put("kou", -16423);  
//	    spellMap.put("ku", -16419);  
//	    spellMap.put("kua", -16412);  
//	    spellMap.put("kuai", -16407);  
//	    spellMap.put("kuan", -16403);  
//	    spellMap.put("kuang", -16401);  
//	    spellMap.put("kui", -16393);  
//	    spellMap.put("kun", -16220);  
//	    spellMap.put("kuo", -16216);  
//	    spellMap.put("la", -16212);  
//	    spellMap.put("lai", -16205);  
//	    spellMap.put("lan", -16202);  
//	    spellMap.put("lang", -16187);  
//	    spellMap.put("lao", -16180);  
//	    spellMap.put("le", -16171);  
//	    spellMap.put("lei", -16169);  
//	    spellMap.put("leng", -16158);  
//	    spellMap.put("li", -16155);  
//	    spellMap.put("lia", -15959);  
//	    spellMap.put("lian", -15958);  
//	    spellMap.put("liang", -15944);  
//	    spellMap.put("liao", -15933);  
//	    spellMap.put("lie", -15920);  
//	    spellMap.put("lin", -15915);  
//	    spellMap.put("ling", -15903);  
//	    spellMap.put("liu", -15889);  
//	    spellMap.put("long", -15878);  
//	    spellMap.put("lou", -15707);  
//	    spellMap.put("lu", -15701);  
//	    spellMap.put("lv", -15681);  
//	    spellMap.put("luan", -15667);  
//	    spellMap.put("lue", -15661);  
//	    spellMap.put("lun", -15659);  
//	    spellMap.put("luo", -15652);  
//	    spellMap.put("ma", -15640);  
//	    spellMap.put("mai", -15631);  
//	    spellMap.put("man", -15625);  
//	    spellMap.put("mang", -15454);  
//	    spellMap.put("mao", -15448);  
//	    spellMap.put("me", -15436);  
//	    spellMap.put("mei", -15435);  
//	    spellMap.put("men", -15419);  
//	    spellMap.put("meng", -15416);  
//	    spellMap.put("mi", -15408);  
//	    spellMap.put("mian", -15394);  
//	    spellMap.put("miao", -15385);  
//	    spellMap.put("mie", -15377);  
//	    spellMap.put("min", -15375);  
//	    spellMap.put("ming", -15369);  
//	    spellMap.put("miu", -15363);  
//	    spellMap.put("mo", -15362);  
//	    spellMap.put("mou", -15183);  
//	    spellMap.put("mu", -15180);  
//	    spellMap.put("na", -15165);  
//	    spellMap.put("nai", -15158);  
//	    spellMap.put("nan", -15153);  
//	    spellMap.put("nang", -15150);  
//	    spellMap.put("nao", -15149);  
//	    spellMap.put("ne", -15144);  
//	    spellMap.put("nei", -15143);  
//	    spellMap.put("nen", -15141);  
//	    spellMap.put("neng", -15140);  
//	    spellMap.put("ni", -15139);  
//	    spellMap.put("nian", -15128);  
//	    spellMap.put("niang", -15121);  
//	    spellMap.put("niao", -15119);  
//	    spellMap.put("nie", -15117);  
//	    spellMap.put("nin", -15110);  
//	    spellMap.put("ning", -15109);  
//	    spellMap.put("niu", -14941);  
//	    spellMap.put("nong", -14937);  
//	    spellMap.put("nu", -14933);  
//	    spellMap.put("nv", -14930);  
//	    spellMap.put("nuan", -14929);  
//	    spellMap.put("nue", -14928);  
//	    spellMap.put("nuo", -14926);  
//	    spellMap.put("'o", -14922);  
//	    spellMap.put("'ou", -14921);  
//	    spellMap.put("pa", -14914);  
//	    spellMap.put("pai", -14908);  
//	    spellMap.put("pan", -14902);  
//	    spellMap.put("pang", -14894);  
//	    spellMap.put("pao", -14889);  
//	    spellMap.put("pei", -14882);  
//	    spellMap.put("pen", -14873);  
//	    spellMap.put("peng", -14871);  
//	    spellMap.put("pi", -14857);  
//	    spellMap.put("pian", -14678);  
//	    spellMap.put("piao", -14674);  
//	    spellMap.put("pie", -14670);  
//	    spellMap.put("pin", -14668);  
//	    spellMap.put("ping", -14663);  
//	    spellMap.put("po", -14654);  
//	    spellMap.put("pu", -14645);  
//	    spellMap.put("qi", -14630);  
//	    spellMap.put("qia", -14594);  
//	    spellMap.put("qian", -14429);  
//	    spellMap.put("qiang", -14407);  
//	    spellMap.put("qiao", -14399);  
//	    spellMap.put("qie", -14384);  
//	    spellMap.put("qin", -14379);  
//	    spellMap.put("qing", -14368);  
//	    spellMap.put("qiong", -14355);  
//	    spellMap.put("qiu", -14353);  
//	    spellMap.put("qu", -14345);  
//	    spellMap.put("quan", -14170);  
//	    spellMap.put("que", -14159);  
//	    spellMap.put("qun", -14151);  
//	    spellMap.put("ran", -14149);  
//	    spellMap.put("rang", -14145);  
//	    spellMap.put("rao", -14140);  
//	    spellMap.put("re", -14137);  
//	    spellMap.put("ren", -14135);  
//	    spellMap.put("reng", -14125);  
//	    spellMap.put("ri", -14123);  
//	    spellMap.put("rong", -14122);  
//	    spellMap.put("rou", -14112);  
//	    spellMap.put("ru", -14109);  
//	    spellMap.put("ruan", -14099);  
//	    spellMap.put("rui", -14097);  
//	    spellMap.put("run", -14094);  
//	    spellMap.put("ruo", -14092);  
//	    spellMap.put("sa", -14090);  
//	    spellMap.put("sai", -14087);  
//	    spellMap.put("san", -14083);  
//	    spellMap.put("sang", -13917);  
//	    spellMap.put("sao", -13914);  
//	    spellMap.put("se", -13910);  
//	    spellMap.put("sen", -13907);  
//	    spellMap.put("seng", -13906);  
//	    spellMap.put("sha", -13905);  
//	    spellMap.put("shai", -13896);  
//	    spellMap.put("shan", -13894);  
//	    spellMap.put("shang", -13878);  
//	    spellMap.put("shao", -13870);  
//	    spellMap.put("she", -13859);  
//	    spellMap.put("shen", -13847);  
//	    spellMap.put("sheng", -13831);  
//	    spellMap.put("shi", -13658);  
//	    spellMap.put("shou", -13611);  
//	    spellMap.put("shu", -13601);  
//	    spellMap.put("shua", -13406);  
//	    spellMap.put("shuai", -13404);  
//	    spellMap.put("shuan", -13400);  
//	    spellMap.put("shuang", -13398);  
//	    spellMap.put("shui", -13395);  
//	    spellMap.put("shun", -13391);  
//	    spellMap.put("shuo", -13387);  
//	    spellMap.put("si", -13383);  
//	    spellMap.put("song", -13367);  
//	    spellMap.put("sou", -13359);  
//	    spellMap.put("su", -13356);  
//	    spellMap.put("suan", -13343);  
//	    spellMap.put("sui", -13340);  
//	    spellMap.put("sun", -13329);  
//	    spellMap.put("suo", -13326);  
//	    spellMap.put("ta", -13318);  
//	    spellMap.put("tai", -13147);  
//	    spellMap.put("tan", -13138);  
//	    spellMap.put("tang", -13120);  
//	    spellMap.put("tao", -13107);  
//	    spellMap.put("te", -13096);  
//	    spellMap.put("teng", -13095);  
//	    spellMap.put("ti", -13091);  
//	    spellMap.put("tian", -13076);  
//	    spellMap.put("tiao", -13068);  
//	    spellMap.put("tie", -13063);  
//	    spellMap.put("ting", -13060);  
//	    spellMap.put("tong", -12888);  
//	    spellMap.put("tou", -12875);  
//	    spellMap.put("tu", -12871);  
//	    spellMap.put("tuan", -12860);  
//	    spellMap.put("tui", -12858);  
//	    spellMap.put("tun", -12852);  
//	    spellMap.put("tuo", -12849);  
//	    spellMap.put("wa", -12838);  
//	    spellMap.put("wai", -12831);  
//	    spellMap.put("wan", -12829);  
//	    spellMap.put("wang", -12812);  
//	    spellMap.put("wei", -12802);  
//	    spellMap.put("wen", -12607);  
//	    spellMap.put("weng", -12597);  
//	    spellMap.put("wo", -12594);  
//	    spellMap.put("wu", -12585);  
//	    spellMap.put("xi", -12556);  
//	    spellMap.put("xia", -12359);  
//	    spellMap.put("xian", -12346);  
//	    spellMap.put("xiang", -12320);  
//	    spellMap.put("xiao", -12300);  
//	    spellMap.put("xie", -12120);  
//	    spellMap.put("xin", -12099);  
//	    spellMap.put("xing", -12089);  
//	    spellMap.put("xiong", -12074);  
//	    spellMap.put("xiu", -12067);  
//	    spellMap.put("xu", -12058);  
//	    spellMap.put("xuan", -12039);  
//	    spellMap.put("xue", -11867);  
//	    spellMap.put("xun", -11861);  
//	    spellMap.put("ya", -11847);  
//	    spellMap.put("yan", -11831);  
//	    spellMap.put("yang", -11798);  
//	    spellMap.put("yao", -11781);  
//	    spellMap.put("ye", -11604);  
//	    spellMap.put("yi", -11589);  
//	    spellMap.put("yin", -11536);  
//	    spellMap.put("ying", -11358);  
//	    spellMap.put("yo", -11340);  
//	    spellMap.put("yong", -11339);  
//	    spellMap.put("you", -11324);  
//	    spellMap.put("yu", -11303);  
//	    spellMap.put("yuan", -11097);  
//	    spellMap.put("yue", -11077);  
//	    spellMap.put("yun", -11067);  
//	    spellMap.put("za", -11055);  
//	    spellMap.put("zai", -11052);  
//	    spellMap.put("zan", -11045);  
//	    spellMap.put("zang", -11041);  
//	    spellMap.put("zao", -11038);  
//	    spellMap.put("ze", -11024);  
//	    spellMap.put("zei", -11020);  
//	    spellMap.put("zen", -11019);  
//	    spellMap.put("zeng", -11018);  
//	    spellMap.put("zha", -11014);  
//	    spellMap.put("zhai", -10838);  
//	    spellMap.put("zhan", -10832);  
//	    spellMap.put("zhang", -10815);  
//	    spellMap.put("zhao", -10800);  
//	    spellMap.put("zhe", -10790);  
//	    spellMap.put("zhen", -10780);  
//	    spellMap.put("zheng", -10764);  
//	    spellMap.put("zhi", -10587);  
//	    spellMap.put("zhong", -10544);  
//	    spellMap.put("zhou", -10533);  
//	    spellMap.put("zhu", -10519);  
//	    spellMap.put("zhua", -10331);  
//	    spellMap.put("zhuai", -10329);  
//	    spellMap.put("zhuan", -10328);  
//	    spellMap.put("zhuang", -10322);  
//	    spellMap.put("zhui", -10315);  
//	    spellMap.put("zhun", -10309);  
//	    spellMap.put("zhuo", -10307);  
//	    spellMap.put("zi", -10296);  
//	    spellMap.put("zong", -10281);  
//	    spellMap.put("zou", -10274);  
//	    spellMap.put("zu", -10270);  
//	    spellMap.put("zuan", -10262);  
//	    spellMap.put("zui", -10260);  
//	    spellMap.put("zun", -10256);  
//	    spellMap.put("zuo", -10254);  
//	  }  
//	 
//	  private static void initUncommonWords(){ 
//	    uncommonWordsMap.put('奡', "ao");  
//	    uncommonWordsMap.put('灞', "ba");  
//	    uncommonWordsMap.put('犇', "ben");  
//	    uncommonWordsMap.put('猋', "biao");  
//	    uncommonWordsMap.put('骉', "biao");  
//	    uncommonWordsMap.put('杈', "cha");  
//	    uncommonWordsMap.put('棽', "chen");  
//	    uncommonWordsMap.put('琤', "cheng");  
//	    uncommonWordsMap.put('魑', "chi");  
//	    uncommonWordsMap.put('蟲', "chong");  
//	    uncommonWordsMap.put('翀', "chong");  
//	    uncommonWordsMap.put('麤', "cu");  
//	    uncommonWordsMap.put('毳', "cui");  
//	    uncommonWordsMap.put('昉', "fang");  
//	    uncommonWordsMap.put('沣', "feng");  
//	    uncommonWordsMap.put('玽', "gou");  
//	    uncommonWordsMap.put('焓', "han");  
//	    uncommonWordsMap.put('琀', "han");  
//	    uncommonWordsMap.put('晗', "han");  
//	    uncommonWordsMap.put('浛', "han");  
//	    uncommonWordsMap.put('翮', "he");  
//	    uncommonWordsMap.put('翯', "he");  
//	    uncommonWordsMap.put('嬛', "huan");  
//	    uncommonWordsMap.put('翙', "hui");  
//	    uncommonWordsMap.put('劼', "jie");  
//	    uncommonWordsMap.put('璟', "jing");  
//	    uncommonWordsMap.put('誩', "jing");  
//	    uncommonWordsMap.put('競', "jing");  
//	    uncommonWordsMap.put('焜', "kun");  
//	    uncommonWordsMap.put('琨', "kun");  
//	    uncommonWordsMap.put('鹍', "kun");  
//	    uncommonWordsMap.put('骊', "li");  
//	    uncommonWordsMap.put('鎏', "liu");  
//	    uncommonWordsMap.put('嫚', "man");  
//	    uncommonWordsMap.put('槑', "mei");  
//	    uncommonWordsMap.put('淼', "miao");  
//	    uncommonWordsMap.put('婻', "nan");  
//	    uncommonWordsMap.put('暔', "nan");  
//	    uncommonWordsMap.put('甯', "ning");  
//	    uncommonWordsMap.put('寗', "ning");  
//	    uncommonWordsMap.put('掱', "pa");  
//	    uncommonWordsMap.put('玭', "pi");  
//	    uncommonWordsMap.put('汧', "qian");  
//	    uncommonWordsMap.put('骎', "qin");  
//	    uncommonWordsMap.put('甠', "qing");  
//	    uncommonWordsMap.put('暒', "qing");  
//	    uncommonWordsMap.put('凊', "qing");  
//	    uncommonWordsMap.put('郬', "qing");  
//	    uncommonWordsMap.put('靘', "qing");  
//	    uncommonWordsMap.put('悫', "que");  
//	    uncommonWordsMap.put('慤', "que");  
//	    uncommonWordsMap.put('瑢', "rong");  
//	    uncommonWordsMap.put('珅', "shen");  
//	    uncommonWordsMap.put('屾', "shen");  
//	    uncommonWordsMap.put('燊', "shen");  
//	    uncommonWordsMap.put('焺', "sheng");  
//	    uncommonWordsMap.put('珄', "sheng");  
//	    uncommonWordsMap.put('晟', "sheng");  
//	    uncommonWordsMap.put('昇', "sheng");  
//	    uncommonWordsMap.put('眚', "sheng");  
//	    uncommonWordsMap.put('湦', "sheng");  
//	    uncommonWordsMap.put('陹', "sheng");  
//	    uncommonWordsMap.put('竔', "sheng");  
//	    uncommonWordsMap.put('琞', "sheng");  
//	    uncommonWordsMap.put('湜', "shi");  
//	    uncommonWordsMap.put('甦', "su");  
//	    uncommonWordsMap.put('弢', "tao");  
//	    uncommonWordsMap.put('瑱', "tian");  
//	    uncommonWordsMap.put('仝', "tong");  
//	    uncommonWordsMap.put('烓', "wei");  
//	    uncommonWordsMap.put('炜', "wei");  
//	    uncommonWordsMap.put('玮', "wei");  
//	    uncommonWordsMap.put('沕', "wu");  
//	    uncommonWordsMap.put('邬', "wu");  
//	    uncommonWordsMap.put('晞', "xi");  
//	    uncommonWordsMap.put('顕', "xian");  
//	    uncommonWordsMap.put('婋', "xiao");  
//	    uncommonWordsMap.put('虓', "xiao");  
//	    uncommonWordsMap.put('筱', "xiao");  
//	    uncommonWordsMap.put('勰', "xie");  
//	    uncommonWordsMap.put('忻', "xin");  
//	    uncommonWordsMap.put('庥', "xiu");  
//	    uncommonWordsMap.put('媭', "xu");  
//	    uncommonWordsMap.put('珝', "xu");  
//	    uncommonWordsMap.put('昫', "xu");  
//	    uncommonWordsMap.put('烜', "xuan");  
//	    uncommonWordsMap.put('煊', "xuan");  
//	    uncommonWordsMap.put('翾', "xuan");  
//	    uncommonWordsMap.put('昍', "xuan");  
//	    uncommonWordsMap.put('暄', "xuan");  
//	    uncommonWordsMap.put('娅', "ya");  
//	    uncommonWordsMap.put('琰', "yan");  
//	    uncommonWordsMap.put('妍', "yan");  
//	    uncommonWordsMap.put('焱', "yan");  
//	    uncommonWordsMap.put('玚', "yang");  
//	    uncommonWordsMap.put('旸', "yang");  
//	    uncommonWordsMap.put('飏', "yang");  
//	    uncommonWordsMap.put('垚', "yao");  
//	    uncommonWordsMap.put('峣', "yao");  
//	    uncommonWordsMap.put('怡', "yi");  
//	    uncommonWordsMap.put('燚', "yi");  
//	    uncommonWordsMap.put('晹', "yi");  
//	    uncommonWordsMap.put('祎', "yi");  
//	    uncommonWordsMap.put('瑛', "ying");  
//	    uncommonWordsMap.put('煐', "ying");  
//	    uncommonWordsMap.put('媖', "ying");  
//	    uncommonWordsMap.put('暎', "ying");  
//	    uncommonWordsMap.put('滢', "ying");  
//	    uncommonWordsMap.put('锳', "ying");  
//	    uncommonWordsMap.put('莜', "you");  
//	    uncommonWordsMap.put('昱', "yu");  
//	    uncommonWordsMap.put('沄', "yun");  
//	    uncommonWordsMap.put('晢', "zhe");  
//	    uncommonWordsMap.put('喆', "zhe");  
//	    uncommonWordsMap.put('臸', "zhi");  
//	  }  
//
//	  
//	  /** 
//	   * 根据ASCII码到SpellMap中查找对应的拼音 
//	   * @param ascii ASCII 
//	   * @return ascii对应的拼音, 如果ascii对应的字符为单字符，则返回对应的单字符, 如果不是单字符且在 spellMap 中没找到对应的拼音，则返回空字符串(""),  
//	   */ 
//	  public static String getSpellByAscii(int ascii) {  
//	    if (ascii > 0 && ascii < 160) { // 单字符  
//	      return String.valueOf((char) ascii);  
//	    }  
//	 
//	    if (ascii < -20319 || ascii > -10247) { // 不知道的字符  
//	      return "";  
//	    }  
//	 
//	    String spell = null; //key  
//	    Integer asciiRang; //value  
//	    String spellPrevious = null; //用来保存上次轮循环的key  
//	    int asciiRangPrevious = -20319; //用来保存上一次循环的value  
//	    for (Iterator it = spellMap.keySet().iterator(); it.hasNext();) {  
//	      spell = (String) it.next(); //拼音  
//	      asciiRang = spellMap.get(spell); //拼音的ASCII  
//	      if(asciiRang != null){  
//	        if (ascii >= asciiRangPrevious && ascii < asciiRang) { // 区间找到, 返回对应的拼音  
//	          return (spellPrevious == null) ? spell : spellPrevious;  
//	        } else {  
//	          spellPrevious = spell;  
//	          asciiRangPrevious = asciiRang;  
//	        }  
//	      }  
//	    }  
//	    return "";  
//	  }  
//	  
//	  /** 
//	   * 获取字符串的全拼或首拼,是汉字则转化为对应的拼音或拼音首字母,其它字符不进行转换 
//	   * @param cnStr 要获取全拼或首拼的字符串 
//	   * @param onlyFirstSpell 是否只获取首拼，为true时，只获取首拼，为false时，获取全拼 
//	   * @return String cnStr的全拼或首拼, 如果 cnStr 为null 时, 返回"" 
//	   */ 
//	  public static String getSpell(String cnStr, boolean onlyFirstSpell) {  
//	    if (cnStr == null) {  
//	      return "";  
//	    }  
//	      
//	    char[] chars = cnStr.trim().toCharArray();  
//	    StringBuffer sb = new StringBuffer();  
//	    for (int i = 0, len = chars.length; i < len; i++) {  
//	      int ascii = getCnAscii(chars[i]);  
//	      if (ascii == 0){ //如果获取汉字的ASCII出错，则不进行转换  
//	        sb.append(chars[i]);  
//	      }else{  
//	        String spell = getSpellByAscii(ascii); //根据ASCII取拼音  
//	        if(spell == null || spell.length() == 0){ //如果根据ASCII取拼音没取到，则到生僻字Map中取  
//	          spell = uncommonWordsMap.get(chars[i]);  
//	        }  
//	          
//	        if(spell == null || spell.length() == 0){ //如果没有取到对应的拼音，则不做转换，追加原字符  
//	          spell = uncommonWordsMap.get(chars[i]);  
//	        }else{  
//	          if(onlyFirstSpell){  
//	            sb.append(spell.startsWith("'") ? spell.substring(1, 2) : spell.substring(0, 1));  
//	          }else{  
//	            sb.append(spell);  
//	          }  
//	        }  
//	      }  
//	    } // end of for  
//	    return sb.toString();  
//	  }  
//	  
//	  
	  
}

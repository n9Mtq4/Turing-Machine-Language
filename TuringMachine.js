TuringMachine = function(code) {
    
    this.code = code;
    this.lines = code.split("\n");
    this.lineNumber = 0;
    this.debugLineNumber = 0;
    
    this.register;
    this.halted = false;
    this.pos = 0;
    this.state = 0;
    
    this.constructor = function() {
        
        this.createRegister();
        this.insertData();
        
    };
    
    this.run = function() {
        
        while (!this.halted) {
            
            var rules = this.findRulesForState(this.state);
            if (rules.length > 2) throw new SyntaxError(rules[rules.length - 1], this.debugLineNumber, "there can only be two rules per state");
            try {
                
                for (var i = 0; i < rules.length; i++) {
                //    replace all
                }
                
                for (var i = 0; i < rules.length; i++) {
                    var tokens = rules[i].split(":");
                    
                    var byteCheck = parseInt(tokens[0]);
                    if (this.register.getAt(this.pos) == byteCheck) {

                        if ("" != !tokens[1].trim().toLowerCase()) {
                            //    new value
                            this.newByte = parseInt(tokens[1]);
                            this.register.setAt(this.pos, newByte);
                        }
                        
                        if (!tokens[2].trim() == "") {
                            //relative moving
                            var relativeMove = parseInt(tokens[2]);
                            this.pos += relativeMove;
                        }
                        
                        if (tokens.length > 3 && tokens[3].trim().toLowerCase() != "") {
                        //    settings a new state
                            var newState = parseInt(tokens[3]);
                            if (newState == -1) this.halted = true;
                            this.state = newState;
                        }
                        
                    }
                    
                }
                
            }catch (e) {
                throw new SyntaxError(rules[rules.length - 1], this.debugLineNumber, e);
            }
            
        }
        
        return this.register;
        
    };
    
    this.findRulesForState = function(state) {
        
        this.resetLine();
        try {
            
            var line;
            var lines = [];
            while ((line = this.nextLine()) != null) {
                if (startsWith(line, ":" + state)) {
                    
                    var line1;
                    while ((line1 = this.nextLine()) != null && !(startsWith(line1, ":")) && line1.trim() != "") {
                        lines.push(line1);
                    }
                    
                }
            }
            
            this.debugLineNumber = this.lineNumber;
            this.resetLine();
            
            if (lines.length == 0) throw new SyntaxError("program", 0, "no state with id " + this.state);
            return lines;
            
        }catch (e) {
            throw new SyntaxError("program", 0, "no state with it " + state);
        }
        
    };
    
    this.createRegister = function() {
        
        var regSize = -1;
        var line;
        while ((line = this.nextLine()) != null) {
            
            if (startsWith(line, "reg ") && line.trim().split(" ").length == 2) {
                var sizeInString = line.trim().split(" ")[1];
                try {
                    regSize = parseInt(sizeInString);
                }catch (e) {
                    throw new SyntaxError(line, this.lineNumber, "register size is not an integer");
                }
            }
            
        }
        
        if (regSize == -1) {
            throw new SyntaxError("program", 0, "no register size specified");
        }
        
        this.register = DataRegister(regSize);
        
    };
    this.insertData = function() {
        
        var line;
        while ((line = this.nextLine()) != null) {
            
            if (startsWith(line, " in")) {
                
                var inputedData = line.split(" ")[1];
                var chars = inputedData.split('');
                var data = new Array(chars.length);
                for (var i = 0; i < chars.length; i++) {
                    data[i] = chars[i] == '0' ? 0 : 1;
                }
                console.log("INSERTING: " + inputedData);
                this.register.insertData(data, 0);
                
            }
            
        }
        
        this.resetLine();
        
    };
    this.nextLine = function() {
        return this.lines[this.lineNumber];
    };
    this.resetLine = function() {
        this.lineNumber = 0;
    };
    
    this.constructor();
    
};

DataRegister = function(size) {
    this.data =  new Array(1 << size);
    console.log("REGISTER SIZE: " + this.data.length);
    this.getAt = function(index) {
        return this.data[index];
    };
    this.setAt = function(index, value) {
        this.data[index] = value;
    };
    this.insertData = function(insert, offSet) {
        for (var i = 0; i < insert.length; i++) {
            this.data[i + offSet] = insert[i];
        }
    };
};

function SyntaxError(lineText, lineNumber, message) {
    this.name = "Syntax Error";
    this.message = ("Syntax Error at line " + lineNumber + "(" + lineText + ") : " + message);
}
SyntaxError.prototype = Error.prototype;

startsWith = function(source, find) {
    return source.lastIndexOf(find, 0) === 0
};
